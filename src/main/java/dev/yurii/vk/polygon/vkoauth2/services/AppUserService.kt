package dev.yurii.vk.polygon.vkoauth2.services

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.objects.UserAuthResponse
import dev.yurii.vk.polygon.persistence.entities.Credentials
import dev.yurii.vk.polygon.persistence.entities.User
import dev.yurii.vk.polygon.persistence.repositories.CredentialsRepository
import dev.yurii.vk.polygon.persistence.repositories.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.OAuth2AuthorizationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.security.oauth2.core.user.OAuth2User
import java.time.ZonedDateTime
import javax.transaction.Transactional

open class AppUserService : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private var logger: Logger = LoggerFactory.getLogger(AppUserService::class.java)

    @Autowired
    private lateinit var credentialsRepository: CredentialsRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var vk: VkApiClient

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val token = userRequest.additionalParameters["vkToken"] as UserAuthResponse

        try {
            var creds = Credentials(
                    entityId = token.userId.toString(),
                    provider = Credentials.CredentialsProvider.VK_PERSONAL
            );

            when (val found = credentialsRepository.findByProviderAndEntityId(creds.provider, creds.entityId)) {
                null -> {
                    creds.accessToken = token.accessToken;
                    creds.entityId = token.userId.toString()
                    creds.expiresAt = when (token.expiresIn) {
                        0 -> null
                        else -> ZonedDateTime.now().plusSeconds(token.expiresIn.toLong());
                    }

                    var user = User(
                            userName = "vk_user_${creds.entityId}",
                            state = User.UserState.OAUTH2_CREATED
                    )

                    user = userRepository.save(user)
                    creds.owner = user
                    creds = credentialsRepository.save(creds)
                }

                else -> {
                    creds = found
                }
            }


            return AppUser(creds.owner!!, creds);
        } catch (ex: Exception) {
            // TODO Introduce better error handling
            ex.printStackTrace()
            logger.error("Error during authentication", logger)

            val oauth2error = OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR)
            throw OAuth2AuthorizationException(oauth2error, ex)
        }
    }
}
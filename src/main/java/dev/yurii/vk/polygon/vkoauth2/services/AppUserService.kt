package dev.yurii.vk.polygon.vkoauth2.services

import com.vk.api.sdk.objects.UserAuthResponse
import dev.yurii.vk.polygon.persistence.entities.User
import dev.yurii.vk.polygon.persistence.entities.UserToken
import dev.yurii.vk.polygon.persistence.repositories.UserTokenRepository
import dev.yurii.vk.polygon.persistence.repositories.UserRepository
import dev.yurii.vk.polygon.vkoauth2.data.AppUser
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
    private lateinit var userTokenRepository: UserTokenRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val token = userRequest.additionalParameters["vkToken"] as UserAuthResponse

        try {
            val userToken =
                    when (val found = userTokenRepository.findByUserId(token.userId)) {
                        null -> {
                            var newToken = UserToken(
                                    userId = token.userId
                            );

                            newToken.accessToken = token.accessToken;

                            var user = User(
                                    userName = "vk_user_${newToken.userId}"
                            )

                            user = userRepository.save(user)

                            newToken.owner = user
                            user.userToken = newToken

                            newToken = userTokenRepository.save(newToken)

                            newToken
                        }

                        else -> {
                            found
                        }
                    }

            return AppUser(userToken.owner!!);
        } catch (ex: Exception) {
            // TODO Introduce better error handling
            ex.printStackTrace()
            logger.error("Error during authentication", logger)

            val oauth2error = OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR)
            throw OAuth2AuthorizationException(oauth2error, ex)
        }
    }
}
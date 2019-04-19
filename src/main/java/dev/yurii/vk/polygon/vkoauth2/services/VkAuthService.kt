package dev.yurii.vk.polygon.vkoauth2.services

import com.vk.api.sdk.client.VkApiClient
import dev.yurii.vk.polygon.persistence.entities.Credentials
import dev.yurii.vk.polygon.persistence.entities.User
import dev.yurii.vk.polygon.persistence.repositories.CredentialsRepository
import dev.yurii.vk.polygon.vkoauth2.auth.AppAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken
import org.springframework.security.oauth2.core.OAuth2AuthorizationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Component
open class VkAuthService {

    @Autowired
    private lateinit var credentialsRepository: CredentialsRepository

    @Autowired
    private lateinit var vk: VkApiClient

    @Transactional
    open fun getOrCreateUser(authToken: OAuth2AuthorizationCodeAuthenticationToken): AppAuthentication {
        val registration = authToken.clientRegistration
        val response = authToken.authorizationExchange.authorizationResponse

        try {
            val token = vk.oauth().userAuthorizationCodeFlow(
                    Integer.parseInt(registration.clientId),
                    registration.clientSecret,
                    response.redirectUri,
                    response.code
            ).execute()

            var creds = Credentials(
                    entityId = token.userId.toString(),
                    provider = Credentials.CredentialsProvider.VK_PERSONAL
            );

            when (val found = credentialsRepository.findOne(Example.of(creds))) {
                null -> {
                    creds.accessToken = token.accessToken;
                    creds.entityId = token.userId.toString()
                    creds.expiresAt = when (token.expiresIn) {
                        0 -> null
                        else -> ZonedDateTime.now().plusSeconds(token.expiresIn.toLong());
                    }

                    val user = User(
                            userName = "vk_user_${creds.entityId}",
                            state = User.UserState.OAUTH2_CREATED
                    )

                    user.credentials.add(creds)
                    creds.owner = user

                    creds = credentialsRepository.save(creds)
                }

                else -> {
                    creds = found
                }
            }

            return AppAuthentication(creds.owner!!, creds);
        } catch (ex: Exception) {
            val oauth2error = OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR)
            throw OAuth2AuthorizationException(oauth2error, ex)
        }

    }
}
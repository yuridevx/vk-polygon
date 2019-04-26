package dev.yurii.vk.schema.vkoauth2.web

import com.vk.api.sdk.client.VkApiClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse

class AppOauth2AccessTokenResponseClient : OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    @Autowired
    private lateinit var vk: VkApiClient

    override fun getTokenResponse(grantRequest: OAuth2AuthorizationCodeGrantRequest): OAuth2AccessTokenResponse {
        val registration = grantRequest.clientRegistration
        val response = grantRequest.authorizationExchange.authorizationResponse

        val request = vk.oauth().userAuthorizationCodeFlow(
                Integer.parseInt(registration.clientId),
                registration.clientSecret,
                response.redirectUri,
                response.code
        )

        val token = request.execute()

        val tokenResponse = OAuth2AccessTokenResponse
                .withToken(token.accessToken)
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .scopes(registration.scopes)
                .expiresIn(token.expiresIn.toLong())
                .additionalParameters(mapOf("vkToken" to token))
                .build()

        return tokenResponse
    }
}
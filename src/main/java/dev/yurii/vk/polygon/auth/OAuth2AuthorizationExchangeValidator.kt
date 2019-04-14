package dev.yurii.vk.polygon.auth

import org.springframework.security.oauth2.core.OAuth2AuthorizationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange

/**
 * A validator for an &quot;exchange&quot; of an OAuth 2.0 Authorization Request and Response.
 *
 * @author Joe Grandja
 * @see OAuth2AuthorizationExchange
 *
 * @since 5.1
 */
object OAuth2AuthorizationExchangeValidator {
    private val INVALID_STATE_PARAMETER_ERROR_CODE = "invalid_state_parameter"
    private val INVALID_REDIRECT_URI_PARAMETER_ERROR_CODE = "invalid_redirect_uri_parameter"

    fun validate(authorizationExchange: OAuth2AuthorizationExchange) {
        val authorizationRequest = authorizationExchange.authorizationRequest
        val authorizationResponse = authorizationExchange.authorizationResponse

        if (authorizationResponse.statusError()) {
            throw OAuth2AuthorizationException(authorizationResponse.error)
        }

        if (authorizationResponse.state != authorizationRequest.state) {
            val oauth2Error = OAuth2Error(INVALID_STATE_PARAMETER_ERROR_CODE)
            throw OAuth2AuthorizationException(oauth2Error)
        }

        if (authorizationResponse.redirectUri != authorizationRequest.redirectUri) {
            val oauth2Error = OAuth2Error(INVALID_REDIRECT_URI_PARAMETER_ERROR_CODE)
            throw OAuth2AuthorizationException(oauth2Error)
        }
    }
}

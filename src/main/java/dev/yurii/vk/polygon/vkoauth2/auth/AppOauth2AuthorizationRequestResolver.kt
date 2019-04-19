/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.yurii.vk.polygon.vkoauth2.auth

import org.springframework.security.crypto.keygen.Base64StringKeyGenerator
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.web.util.UrlUtils
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.util.UriComponentsBuilder
import java.util.*
import javax.servlet.http.HttpServletRequest

class AppOauth2AuthorizationRequestResolver(private val clientRegistrationRepository: ClientRegistrationRepository) : OAuth2AuthorizationRequestResolver {
    private val authorizationRequestMatcher: AntPathRequestMatcher
    private val stateGenerator = Base64StringKeyGenerator(Base64.getUrlEncoder())

    init {
        this.authorizationRequestMatcher = AntPathRequestMatcher(
                OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/{" + REGISTRATION_ID_URI_VARIABLE_NAME + "}")
    }

    override fun resolve(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        val registrationId = this.resolveRegistrationId(request)
        val redirectUriAction = getAction(request, "login")

        return resolve(request, registrationId, redirectUriAction)
    }

    override fun resolve(request: HttpServletRequest, registrationId: String?): OAuth2AuthorizationRequest? {
        if (registrationId == null) {
            return null
        }
        val redirectUriAction = getAction(request, "authorize")
        return resolve(request, registrationId, redirectUriAction)
    }

    private fun getAction(request: HttpServletRequest, defaultAction: String): String {
        return request.getParameter("action") ?: defaultAction
    }

    private fun resolve(request: HttpServletRequest, registrationId: String?, redirectUriAction: String): OAuth2AuthorizationRequest? {
        if (registrationId == null) {
            return null
        }

        val clientRegistration = this.clientRegistrationRepository.findByRegistrationId(registrationId)
                ?: throw IllegalArgumentException("Invalid Client Registration with Id: $registrationId")

        val builder: OAuth2AuthorizationRequest.Builder

        if (AuthorizationGrantType.AUTHORIZATION_CODE == clientRegistration.authorizationGrantType) {
            builder = OAuth2AuthorizationRequest.authorizationCode()
        } else if (AuthorizationGrantType.IMPLICIT == clientRegistration.authorizationGrantType) {
            builder = OAuth2AuthorizationRequest.implicit()
        } else {
            throw IllegalArgumentException(("Invalid Authorization Grant Type (" +
                    clientRegistration.authorizationGrantType.value +
                    ") for Client Registration with Id: " + clientRegistration.registrationId))
        }

        val redirectUriStr = this.expandRedirectUri(request, clientRegistration, redirectUriAction)

        val additionalParameters = HashMap<String, Any>()
        additionalParameters.put(OAuth2ParameterNames.REGISTRATION_ID, clientRegistration.registrationId)

        val groupIds = request.getParameter("group_ids")

        if (groupIds != null) {
            additionalParameters["group_ids"] = groupIds
        }

        val mergedScope = HashSet<String>(clientRegistration.scopes)
        val requestScope = request.getParameterValues("scope")

        if (requestScope != null) {
            mergedScope.addAll(Arrays.asList<String>(*requestScope))
        }

        val authorizationRequest = builder
                .clientId(clientRegistration.clientId)
                .authorizationUri(clientRegistration.providerDetails.authorizationUri)
                .redirectUri(redirectUriStr)
                .scopes(mergedScope)
                .state(this.stateGenerator.generateKey())
                .additionalParameters(additionalParameters)
                .build()

        return authorizationRequest
    }

    private fun resolveRegistrationId(request: HttpServletRequest): String? {
        if (this.authorizationRequestMatcher.matches(request)) {
            return this.authorizationRequestMatcher
                    .extractUriTemplateVariables(request)[REGISTRATION_ID_URI_VARIABLE_NAME]
        }
        return null
    }

    private fun expandRedirectUri(request: HttpServletRequest, clientRegistration: ClientRegistration, action: String?): String {
        // Supported URI variables -> baseUrl, action, registrationId
        // Used in -> CommonOAuth2Provider.DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}"
        val uriVariables = HashMap<String, String>()
        uriVariables["registrationId"] = clientRegistration.registrationId

        val baseUrl = UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
                .replaceQuery(null)
                .replacePath(request.contextPath)
                .build()
                .toUriString()
        uriVariables["baseUrl"] = baseUrl
        if (action != null) {
            uriVariables["action"] = action
        }
        return UriComponentsBuilder.fromUriString(clientRegistration.redirectUriTemplate)
                .buildAndExpand(uriVariables)
                .toUriString()
    }

    companion object {
        private const val REGISTRATION_ID_URI_VARIABLE_NAME = "registrationId"
    }
}

package dev.yurii.vk.schema.vkoauth2

import dev.yurii.vk.schema.vkoauth2.services.AppUserService
import dev.yurii.vk.schema.vkoauth2.web.AppOauth2AccessTokenResponseClient
import dev.yurii.vk.schema.vkoauth2.web.VKOauth2AuthenticationExceptionFilter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod

@Configuration
@Order(10)
class VKOauth2Configuration : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    public override fun configure(http: HttpSecurity) {

        http.oauth2Login()
                .clientRegistrationRepository(registrationRepository())
                .authorizationEndpoint()
                .and()
                .redirectionEndpoint()
                .and()
                .tokenEndpoint()
                .accessTokenResponseClient(responseClient())
                .and()
                .userInfoEndpoint()
                .userService(appUserService())
                .and()

        http.addFilterBefore(groupFilter(), OAuth2AuthorizationRequestRedirectFilter::class.java)
    }

    @Bean
    fun responseClient(): AppOauth2AccessTokenResponseClient {
        return AppOauth2AccessTokenResponseClient()
    }

    @Bean
    fun appUserService(): AppUserService {
        return AppUserService()
    }

    @Bean
    fun groupFilter(): VKOauth2AuthenticationExceptionFilter {
        return VKOauth2AuthenticationExceptionFilter()
    }

    @Bean
    fun registrationRepository(): InMemoryClientRegistrationRepository {
        return InMemoryClientRegistrationRepository(vkClientRegistration())
    }

    @Bean
    @Qualifier("vk_personal")
    fun vkClientRegistration(): ClientRegistration {
        return ClientRegistration
                .withRegistrationId("vk_personal")
                .authorizationUri("https://oauth.vk.com/authorize")
                .tokenUri("https://oauth.vk.com/access_token")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
                .clientName("vk_personal")
                .clientId("6935719")
                .clientSecret("jSDobEhvSYW0JYCgelqc")
                .userNameAttributeName("id")
                .scope("ads", "offline", "email", "groups", "users")
                .build()
    }
}

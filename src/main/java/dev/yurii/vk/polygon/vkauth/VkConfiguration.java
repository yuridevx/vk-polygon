package dev.yurii.vk.polygon.vkauth;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import lombok.var;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

@Configuration
public class VkConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Client()
                .clientRegistrationRepository(registrationRepository())
                .authorizationCodeGrant()
                .authorizationRequestResolver(requestResolver())
                .accessTokenResponseClient(responseClient());
    }

    @Bean
    protected AppOauth2AccessTokenResponseClient responseClient() {
        return new AppOauth2AccessTokenResponseClient();
    }

    @Bean
    protected AppOauth2AuthorizationRequestResolver requestResolver() {
        return new AppOauth2AuthorizationRequestResolver(registrationRepository());
    }

    @Bean
    protected InMemoryClientRegistrationRepository registrationRepository() {
        var personal = ClientRegistration
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
                .userInfoUri("https://api.vk.com/method/users.get")
                .build();

        var groups = ClientRegistration
                .withRegistrationId("vk_group")
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
                .userInfoUri("https://api.vk.com/method/users.get")
                .build();

        return new InMemoryClientRegistrationRepository(personal, groups);
    }

    @Bean
    public VkApiClient vkApiClient() {
        return new VkApiClient(new HttpTransportClient());
    }

}

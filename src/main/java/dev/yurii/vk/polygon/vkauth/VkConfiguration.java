package dev.yurii.vk.polygon.vkauth;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import dev.yurii.vk.polygon.vkauth.converter.UserServiceRequestConverter;
import dev.yurii.vk.polygon.vkauth.converter.TokenClientRequestConverter;
import dev.yurii.vk.polygon.vkauth.converter.TokenClientResponseConverter;
import dev.yurii.vk.polygon.vkauth.converter.VkOauthUserService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class VkConfiguration extends WebSecurityConfigurerAdapter {

    private UserServiceRequestConverter userServiceRequestConverter = new UserServiceRequestConverter();

    private TokenClientRequestConverter tokenClientRequestConverter = new TokenClientRequestConverter();

    private TokenClientResponseConverter tokenClientResponseConverter = new TokenClientResponseConverter();

    @Bean
    public VkApiClient createClient() {
        var transport = new HttpTransportClient();
        var client = new VkApiClient(transport);

        return client;
    }

    //TODO Implement better userInfoEndpoint error handler for better stability

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement();
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .tokenEndpoint()
                .accessTokenResponseClient(buildVkClient())
                .and()
                .userInfoEndpoint()
                .userService(userService());
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(vkRegistration());
    }

    public OAuth2UserService<OAuth2UserRequest, OAuth2User> userService() {
        return new VkOauthUserService();
    }

    public DefaultAuthorizationCodeTokenResponseClient buildVkClient() {
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient =
                new DefaultAuthorizationCodeTokenResponseClient();

        accessTokenResponseClient.setRequestEntityConverter(tokenClientRequestConverter);

        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
                new FormHttpMessageConverter(), tokenClientResponseConverter));

        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        accessTokenResponseClient.setRestOperations(restTemplate);

        return accessTokenResponseClient;
    }


    protected ClientRegistration vkRegistration() {
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
                .userInfoUri("https://api.vk.com/method/users.get")
                .build();
    }

}

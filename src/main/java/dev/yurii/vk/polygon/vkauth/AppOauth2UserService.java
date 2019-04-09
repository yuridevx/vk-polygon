package dev.yurii.vk.polygon.vkauth;

import lombok.var;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;


@Component
public class AppOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        var user = new VkOauth2User();

        user.setToken(userRequest.getAccessToken());
        user.setAttributes(userRequest.getAdditionalParameters());
        user.setUserId((Integer) userRequest.getAdditionalParameters().get("id"));
        user.setEmail((String) userRequest.getAdditionalParameters().get("email"));
        user.setUserName(userRequest.getClientRegistration().getClientName() + "-" + userRequest.getAdditionalParameters().get("id"));

        return user;
    }
}

package dev.yurii.vk.polygon.api;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import dev.yurii.vk.polygon.vkauth.VkOauth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Provider;

@RestController
public class MeController {


    @RequestMapping("/groups")
    public Object user(@AuthenticationPrincipal VkOauth2User user) {
        return user;
    }
}

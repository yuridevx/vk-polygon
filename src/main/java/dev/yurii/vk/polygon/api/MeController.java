package dev.yurii.vk.polygon.api;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Provider;

@RestController
public class MeController {

    @Autowired
    private VkApiClient vk;

    @Autowired
    private Provider<UserActor> user;

    @RequestMapping("/groups")
    public Object user(OAuth2AuthenticationToken token) {
        return vk.groups().get(user.get());
    }
}

package dev.yurii.vk.polygon.api;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class MeController {


    @RequestMapping("/me")
    public Object user(OAuth2AuthenticationToken user) {
        return user;
    }
}

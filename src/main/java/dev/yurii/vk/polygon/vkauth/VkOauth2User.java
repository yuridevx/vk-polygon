package dev.yurii.vk.polygon.vkauth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vk.api.sdk.client.actors.UserActor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Data
public class VkOauth2User implements OAuth2User {

    private String userName;
    private String email;
    private int userId;

    private Map<String, Object> attributes = new HashMap<>();
    private Collection<? extends GrantedAuthority> authorities = new HashSet<>();

    @JsonIgnore
    private OAuth2AccessToken token;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return userName;
    }

    @JsonIgnore
    public UserActor getUserActor() {
        return new UserActor(userId, token.getTokenValue());
    }
}

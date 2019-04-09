package dev.yurii.vk.polygon.vkauth.converter;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class UserServiceRequestConverter implements Converter<OAuth2UserRequest, RequestEntity<?>> {

    @Override
    public RequestEntity<?> convert(OAuth2UserRequest source) {
        ClientRegistration registration = source.getClientRegistration();
        var uri = UriComponentsBuilder
                .fromUriString(registration.getProviderDetails().getUserInfoEndpoint().getUri())
                .queryParam("access_token", source.getAccessToken().getTokenValue())
                .queryParam("v", "5.92")
                .build()
                .toUri();
        var entity = new RequestEntity(HttpMethod.GET, uri);
        return entity;
    }
}

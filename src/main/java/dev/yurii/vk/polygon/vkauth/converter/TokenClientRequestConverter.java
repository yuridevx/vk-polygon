package dev.yurii.vk.polygon.vkauth.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@Component
public class TokenClientRequestConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {
    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest source) {
        ClientRegistration clientRegistration = source.getClientRegistration();

        URI uri = UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getTokenUri())
                .queryParam("client_id", clientRegistration.getClientId())
                .queryParam("client_secret", clientRegistration.getClientSecret())
                .queryParam("code", source.getAuthorizationExchange().getAuthorizationResponse().getCode())
                .queryParam("redirect_uri", source.getAuthorizationExchange().getAuthorizationResponse().getRedirectUri())
//                .queryParam("scope", clientRegistration.getScopes().toArray())
                .build()
                .toUri();

        return new RequestEntity<>(HttpMethod.GET, uri);
    }
}

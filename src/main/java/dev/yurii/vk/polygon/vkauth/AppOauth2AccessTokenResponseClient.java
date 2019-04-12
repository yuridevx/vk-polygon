package dev.yurii.vk.polygon.vkauth;

import com.vk.api.sdk.client.VkApiClient;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;

public class AppOauth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    @Autowired
    private VkApiClient vk;

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        var registration = authorizationGrantRequest.getClientRegistration();
        var response = authorizationGrantRequest.getAuthorizationExchange().getAuthorizationResponse();
        try {
            var token = vk.oauth().userAuthorizationCodeFlow(
                    Integer.parseInt(registration.getClientId()),
                    registration.getClientSecret(),
                    response.getRedirectUri(),
                    response.getCode()
            ).execute();
            var map = new HashMap<String, Object>();

            map.put("email", token.getEmail());
            map.put("id", token.getUserId());

            var res = OAuth2AccessTokenResponse.
                    withToken(token.getAccessToken())
                    .tokenType(OAuth2AccessToken.TokenType.BEARER)
                    .expiresIn(token.getExpiresIn())
                    .additionalParameters(map)
                    .build();

            return res;
        } catch (Exception ex) {
            var oauth2error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR);
            throw new OAuth2AuthorizationException(oauth2error, ex);
        }
    }
}

package dev.yurii.vk.polygon.vkauth.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class TokenClientResponseConverter extends AbstractHttpMessageConverter<OAuth2AccessTokenResponse> {

    public TokenClientResponseConverter() {
        super(MediaType.APPLICATION_JSON);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return OAuth2AccessTokenResponse.class.isAssignableFrom(clazz);
    }

    @Override
    protected OAuth2AccessTokenResponse readInternal(Class<? extends OAuth2AccessTokenResponse> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        var mapper = new ObjectMapper();
        var tree = mapper.readTree(inputMessage.getBody());
        var map = new HashMap<String, Object>();

        map.put("user_id", tree.get("user_id").asText());

        var token = OAuth2AccessTokenResponse
                .withToken(tree.get("access_token").textValue())
                .expiresIn(tree.get("expires_in").asLong())
                .additionalParameters(map)
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .build();

        return token;
    }

    @Override
    protected void writeInternal(OAuth2AccessTokenResponse oAuth2AccessTokenResponse, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        throw new UnsupportedOperationException();
    }
}

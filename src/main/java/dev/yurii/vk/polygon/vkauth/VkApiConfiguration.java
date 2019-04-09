package dev.yurii.vk.polygon.vkauth;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkApiConfiguration {

    @Bean
    public VkApiClient vkApiClient() {
        return new VkApiClient(new HttpTransportClient());
    }
}

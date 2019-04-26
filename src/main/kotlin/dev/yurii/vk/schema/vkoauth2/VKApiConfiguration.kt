package dev.yurii.vk.schema.vkoauth2

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.httpclient.HttpTransportClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VKApiConfiguration {

    @Bean
    fun vkApi(): VkApiClient {
        return VkApiClient(HttpTransportClient())
    }
}

package dev.yurii.vk.polygon.vkoauth2

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.httpclient.HttpTransportClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class VKApiConfiguration {

    @Bean
    open fun vkApi(): VkApiClient {
        return VkApiClient(HttpTransportClient())
    }
}

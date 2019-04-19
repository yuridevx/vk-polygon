package dev.yurii.vk.polygon.vkoauth2

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.httpclient.HttpTransportClient
import dev.yurii.vk.polygon.vkoauth2.resolvers.UserActorResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class VKWebConfiguration : WebMvcConfigurer {

    @Bean
    open fun vkApiClient(): VkApiClient {
        return VkApiClient(HttpTransportClient())
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(UserActorResolver())
    }
}

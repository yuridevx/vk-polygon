package dev.yurii.vk.polygon

import dev.yurii.vk.polygon.vkoauth2.web.UserActorResolver
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class WebConfiguration : WebMvcConfigurer {

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer
                .ignoreAcceptHeader(true)
                .defaultContentType(MediaType.APPLICATION_JSON_UTF8)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(UserActorResolver())
    }
}

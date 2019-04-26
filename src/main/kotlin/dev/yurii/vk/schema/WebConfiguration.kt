package dev.yurii.vk.schema

import dev.yurii.vk.schema.vkoauth2.web.UserActorResolver
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableConfigurationProperties(PlatformProperties::class)
class WebConfiguration : WebMvcConfigurer {

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer
                .ignoreAcceptHeader(true)
                .defaultContentType(MediaType.APPLICATION_JSON_UTF8)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userActorResolver())
    }

    @Bean
    fun userActorResolver(): UserActorResolver {
        return UserActorResolver()
    }
}

package dev.yurii.vk.schema

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("platform")
data class PlatformProperties
(
        var callbackOrigin: String? = null,
        var appOrigin: String? = null
)
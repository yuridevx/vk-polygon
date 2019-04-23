package dev.yurii.vk.polygon.vkoauth2.data

import org.springframework.hateoas.ResourceSupport

class VKAuthErrorResource(
        val message: String,
        val groupId: Int? = null
) : ResourceSupport()

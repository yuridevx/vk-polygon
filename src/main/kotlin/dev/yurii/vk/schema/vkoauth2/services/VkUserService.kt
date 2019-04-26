package dev.yurii.vk.schema.vkoauth2.services

import dev.yurii.vk.schema.relational.entities.GroupToken
import dev.yurii.vk.schema.relational.entities.User
import dev.yurii.vk.schema.vkoauth2.data.AppOauth2User
import dev.yurii.vk.schema.vkoauth2.data.GroupAuthRequiredException
import dev.yurii.vk.schema.vkoauth2.data.UserAuthRequiredException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class VkUserService {

    fun ensureGroupAuthenticated(groupId: Int): GroupToken {
        val user = ensureUserAuthenticated()

        return user.findGroupToken(groupId)
                ?: throw GroupAuthRequiredException(groupId)
    }


    fun ensureUserAuthenticated(): User {
        val auth = SecurityContextHolder.getContext().authentication.principal as? AppOauth2User
                ?: throw UserAuthRequiredException()

        return auth.user
    }
}

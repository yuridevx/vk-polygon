package dev.yurii.vk.polygon.vkoauth2.services

import com.vk.api.sdk.objects.GroupAuthResponse
import dev.yurii.vk.polygon.persistence.entities.GroupToken
import dev.yurii.vk.polygon.persistence.entities.UserToken
import dev.yurii.vk.polygon.persistence.repositories.GroupTokenRepository
import dev.yurii.vk.polygon.vkoauth2.data.AppUser
import dev.yurii.vk.polygon.vkoauth2.data.VKGroupAuthData
import dev.yurii.vk.polygon.vkoauth2.exceptions.GroupAuthRequiredException
import dev.yurii.vk.polygon.vkoauth2.exceptions.UserAuthRequiredException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
open class AppVkAuthService {

    @Autowired
    private lateinit var groupRepo: GroupTokenRepository

    fun ensureGroupAuthenticated(groupId: Int): GroupToken {
        val user = ensureUserAuthenticated()

        val cred = user.findGroupToken(groupId)
                ?: throw GroupAuthRequiredException(groupId)

        return cred;
    }


    fun ensureUserAuthenticated(): AppUser {
        val auth = SecurityContextHolder.getContext().authentication.principal as? AppUser
                ?: throw UserAuthRequiredException()

        return auth;
    }


    @Transactional
    open fun getOrCreateGroupToken(data: VKGroupAuthData, auth: GroupAuthResponse): GroupToken {
        val appUser = ensureUserAuthenticated()
        val token = auth.accessTokens[data.groupId]!!

        val creds =
                when (val found = appUser.findGroupToken(data.groupId)) {
                    null -> {
                        var newToken = GroupToken(
                                owner = appUser.user,
                                groupId = data.groupId,
                                accessToken = token
                        )

                        newToken = groupRepo.save(newToken)

                        assert(newToken.owner == appUser.user)

                        newToken.owner!!.groupTokens.add(newToken)

                        newToken
                    }
                    else -> {
                        found
                    }
                }

        return creds
    }
}

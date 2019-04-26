package dev.yurii.vk.schema.vkoauth2.services

import com.vk.api.sdk.objects.GroupAuthResponse
import com.vk.api.sdk.objects.UserAuthResponse
import dev.yurii.vk.schema.persistence.entities.GroupToken
import dev.yurii.vk.schema.persistence.entities.User
import dev.yurii.vk.schema.persistence.entities.UserToken
import dev.yurii.vk.schema.persistence.repositories.UserTokenRepository
import dev.yurii.vk.schema.vkoauth2.data.AppUser
import dev.yurii.vk.schema.vkoauth2.data.VKGroupAuthData
import dev.yurii.vk.schema.vkoauth2.exceptions.GroupAuthRequiredException
import dev.yurii.vk.schema.vkoauth2.exceptions.UserAuthRequiredException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Component
class AppVkAuthService {

    @Autowired
    private lateinit var userTokenRepo: UserTokenRepository

    @Autowired
    private lateinit var manager: EntityManager

    fun ensureGroupAuthenticated(groupId: Int): GroupToken {
        val user = ensureUserAuthenticated()

        val groupToken = user.findGroupToken(groupId)
                ?: throw GroupAuthRequiredException(groupId)

        return groupToken
    }


    fun ensureUserAuthenticated(): User {
        val auth = SecurityContextHolder.getContext().authentication.principal as? AppUser
                ?: throw UserAuthRequiredException()

        return auth.user
    }


    @Transactional
    fun getOrCreateGroupToken(data: VKGroupAuthData, auth: GroupAuthResponse): GroupToken {
        val user = ensureUserAuthenticated()
        val token = auth.accessTokens[data.groupId]!!

        val groupToken =
                when (val found = user.findGroupToken(data.groupId)) {
                    null -> {
                        val newToken = GroupToken(
                                owner = user,
                                groupId = data.groupId,
                                accessToken = token
                        )

                        user.groupTokens.add(newToken)

                        manager.persist(newToken)
                        manager.flush()

                        newToken
                    }
                    else -> {
                        found
                    }
                }

        return groupToken
    }


    @Transactional
    fun getOrCreateUser(userRequest: OAuth2UserRequest): User {
        val token = userRequest.additionalParameters["vkToken"] as UserAuthResponse

        val userToken =
                when (val found = userTokenRepo.findByUserId(token.userId)) {
                    null -> {
                        val user = User(
                                userName = "vk_user_${token.userId}"
                        )

                        val newToken = UserToken(
                                userId = token.userId,
                                accessToken = token.accessToken,
                                owner = user
                        )

                        user.userToken = newToken

                        manager.persist(user)
                        manager.persist(newToken)
                        manager.flush()

                        newToken
                    }

                    else -> {
                        found
                    }
                }

        return userToken.owner!!
    }
}

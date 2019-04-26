package dev.yurii.vk.schema.relational.services

import com.vk.api.sdk.objects.UserAuthResponse
import dev.yurii.vk.schema.relational.entities.GroupToken
import dev.yurii.vk.schema.relational.entities.User
import dev.yurii.vk.schema.relational.entities.UserToken
import dev.yurii.vk.schema.relational.repositories.UserTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Component
class AppAuthService {

    @Autowired
    private lateinit var userTokenRepo: UserTokenRepository

    @Autowired
    private lateinit var manager: EntityManager

    @Transactional
    fun getOrCreateGroupToken(user: User, token: String, groupId: Int): GroupToken {
        return when (val found = user.findGroupToken(groupId)) {
            null -> {
                val newToken = GroupToken(
                        owner = user,
                        groupId = groupId,
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
    }

    @Transactional
    fun getOrCreateUser(token: UserAuthResponse): User {
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

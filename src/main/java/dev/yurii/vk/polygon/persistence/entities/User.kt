package dev.yurii.vk.polygon.persistence.entities

import com.vk.api.sdk.client.actors.UserActor
import org.springframework.util.Assert
import javax.persistence.*

@Entity
@Table(name = "AppUser")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(nullable = false, unique = true)
        var userName: String? = null,

        @OneToOne(mappedBy = "owner", fetch = FetchType.EAGER)
        var userToken: UserToken? = null,

        @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
        var groupTokens: MutableList<GroupToken> = arrayListOf()
) {
    fun findGroupToken(groupId: Int): GroupToken? {
        for (token in groupTokens) {
            if (token.groupId == groupId) {
                return token
            }
        }
        return null
    }

    fun toUserActor(): UserActor {
        Assert.notNull(userToken, "User token must exists to create UserActor")
        return userToken!!.toUserActor()
    }
}
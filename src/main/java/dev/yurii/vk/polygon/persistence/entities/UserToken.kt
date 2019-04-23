package dev.yurii.vk.polygon.persistence.entities

import com.vk.api.sdk.client.actors.UserActor
import javax.persistence.*

@Entity
@Table(
        indexes = [
            Index(columnList = "userId")
        ]
)
data class UserToken(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(nullable = false)
        var userId: Int? = null,

        @Column(nullable = false)
        var accessToken: String? = null,

        @OneToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "ownerId", nullable = false)
        var owner: User? = null
) {

    fun toUserActor(): UserActor {
        return UserActor(userId, accessToken)
    }
}

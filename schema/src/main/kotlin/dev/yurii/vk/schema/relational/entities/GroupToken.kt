package dev.yurii.vk.schema.relational.entities

import com.vk.api.sdk.client.actors.GroupActor
import javax.persistence.*

@Entity
@Table(
        indexes = [
            Index(columnList = "groupId")
        ]
)
data class GroupToken(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(nullable = false)
        var groupId: Int? = null,

        @Column(nullable = false)
        var accessToken: String? = null,

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "ownerId", nullable = false)
        var owner: User? = null
) {

    fun toGroupActor(): GroupActor {
        return GroupActor(groupId, accessToken)
    }
}
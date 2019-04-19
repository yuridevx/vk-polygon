package dev.yurii.vk.polygon.persistence.entities

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(
        uniqueConstraints = [
            UniqueConstraint(columnNames = ["provider", "entityId"]),
            UniqueConstraint(columnNames = ["ownerId", "provider", "entityId"])
        ],
        indexes = [
            Index(columnList = "accessToken,refreshToken")
        ]
)
data class Credentials(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var provider: CredentialsProvider? = null,

        @Column(nullable = false)
        var entityId: String? = null,

        @Column(nullable = false)
        var accessToken: String? = null,

        @Column
        var refreshToken: String? = null,

        @Column
        var expiresAt: ZonedDateTime? = null,

        @Column(nullable = false)
        private var lastUsed: ZonedDateTime? = null,

        @ManyToOne
        @JoinColumn(name = "ownerId", nullable = false)
        var owner: User? = null
) {
    enum class CredentialsProvider {
        VK_PERSONAL,
        VK_GROUP,
        VK_APP
    }

    @PreUpdate
    fun updateLastUsed() {
        lastUsed = ZonedDateTime.now()
    }
}

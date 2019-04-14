package dev.yurii.vk.polygon.persistence.entities

import javax.persistence.*

@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(nullable = false, unique = true)
        var userName: String? = null,

        @OneToMany(mappedBy = "owner")
        var credentials: Set<Credentials>? = HashSet(),

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var state: UserState = UserState.UNKNOWN
) {

    enum class UserState {
        UNKNOWN,
        OAUTH2_CREATED,
        VERIFIED
    }
}

package dev.yurii.vk.polygon.persistence.entities

import javax.persistence.*

@Entity
data class OrganisationUsers(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(nullable = false)
        var organisation: Organisation? = null,

        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(nullable = false)
        var user: User? = null,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var role: UserRole = UserRole.ROLE_GUEST
) {
    enum class UserRole {
        ROLE_OWNER,
        ROLE_GUEST
    }

}

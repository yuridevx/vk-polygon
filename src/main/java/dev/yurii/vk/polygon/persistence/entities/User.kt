package dev.yurii.vk.polygon.persistence.entities

import java.util.*
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
)

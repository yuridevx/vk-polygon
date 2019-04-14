package dev.yurii.vk.polygon.persistence.entities

import javax.persistence.*

@Entity
data class Organisation(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "organisation")
        var credentials: Set<OrganisationCredentials> = HashSet(),

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "organisation")
        var users: Set<OrganisationUsers> = HashSet(),

        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(nullable = false)
        var owner: User? = null
)

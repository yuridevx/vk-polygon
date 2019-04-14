package dev.yurii.vk.polygon.persistence.entities

import lombok.Getter
import lombok.Setter

import javax.persistence.*

@Entity
data class OrganisationCredentials(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @ManyToOne
        @JoinColumn(nullable = false)
        var organisation: Organisation? = null,

        @ManyToOne
        @JoinColumn(nullable = false)
        var credentials: Credentials? = null
)

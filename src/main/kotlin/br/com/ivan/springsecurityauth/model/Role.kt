package br.com.ivan.springsecurityauth.model

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    val name: RoleName
)

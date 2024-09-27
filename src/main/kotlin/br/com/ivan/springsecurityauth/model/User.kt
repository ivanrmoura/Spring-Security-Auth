package br.com.ivan.springsecurityauth.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true)
    val email: String,
    val name: String? = null,
    val password: String,
    val enable: Boolean = true,

    //@OneToOne(cascade = [CascadeType.ALL])
    @OneToOne
    val image: Image? = null,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")])
    val roles: MutableList<Role>
)

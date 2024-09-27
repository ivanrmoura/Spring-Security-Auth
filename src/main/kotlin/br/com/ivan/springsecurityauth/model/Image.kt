package br.com.ivan.springsecurityauth.model

import jakarta.persistence.*

@Entity
@Table(name = "images")
class Image (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val url: String? = null
){
}

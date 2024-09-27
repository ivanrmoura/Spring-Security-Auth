package br.com.ivan.springsecurityauth.dto

import br.com.ivan.springsecurityauth.model.Role


data class RecoveryUserDto(
    val id: Long? = null,
    val name: String,
    val email: String,
    val roles: List<Role>
)

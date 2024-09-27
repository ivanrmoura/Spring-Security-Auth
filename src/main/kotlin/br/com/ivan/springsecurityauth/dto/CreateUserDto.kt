package br.com.ivan.springsecurityauth.dto



data class CreateUserDto (
    val name: String,
    val email: String,
    val password: String,
    val roleId: Long
)

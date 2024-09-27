package br.com.ivan.springsecurityauth.dto

data class RecoveryJwtTokenDto(
    val token: String,
    val refreshToken: String,
    val user: RecoveryUserDto? = null
)

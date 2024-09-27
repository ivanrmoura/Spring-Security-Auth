package br.com.ivan.springsecurityauth.mapper

import br.com.ivan.springsecurityauth.dto.RecoveryUserDto
import br.com.ivan.springsecurityauth.model.User
import org.springframework.stereotype.Component

@Component
class RecoveryUserDtoMapper: Mapper<User, RecoveryUserDto> {

    override fun map(t: User): RecoveryUserDto {

       return RecoveryUserDto(
           id = t.id,
           email = t.email,
           name = t.name ?: "",
           roles = t.roles
       )
    }
}

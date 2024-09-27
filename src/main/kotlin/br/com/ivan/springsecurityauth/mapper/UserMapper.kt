package br.com.ivan.springsecurityauth.mapper

import br.com.ivan.springsecurityauth.dto.CreateUserDto
import br.com.ivan.springsecurityauth.model.User
import br.com.ivan.springsecurityauth.repository.RoleRepository
import br.com.ivan.springsecurityauth.security.config.SecurityConfiguration
import org.springframework.stereotype.Component

@Component
class UserMapper(
    private val roleRepository: RoleRepository,
    private val securityConfiguration: SecurityConfiguration
): Mapper<CreateUserDto, User> {
    override fun map(t: CreateUserDto): User {
      val role = roleRepository.findById(t.roleId).orElse(null)
        return User(
          name = t.name,
          email = t.email,
          password = securityConfiguration.passwordEncoder().encode(t.password),
          roles = mutableListOf(
              role
          )
      )
    }
}

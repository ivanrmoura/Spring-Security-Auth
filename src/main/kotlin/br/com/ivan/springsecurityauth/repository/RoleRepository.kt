package br.com.ivan.springsecurityauth.repository

import br.com.ivan.springsecurityauth.model.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository: JpaRepository<Role, Long> {

}

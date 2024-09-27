package br.com.ivan.springsecurityauth.repository


import br.com.ivan.springsecurityauth.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository: JpaRepository<User, Long> {

    fun findByEmail(email: String): Optional<User>

}

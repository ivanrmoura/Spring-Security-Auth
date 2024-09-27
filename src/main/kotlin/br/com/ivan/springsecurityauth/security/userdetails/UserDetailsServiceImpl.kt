package br.com.ivan.springsecurityauth.security.userdetails


import br.com.ivan.springsecurityauth.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl : UserDetailsService{

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails{
        val user = userRepository.findByEmail(username).orElseThrow {
            RuntimeException("Usuário não encontrado")
        }
        return UserDetailsImpl(user)
    }
}

package br.com.ivan.springsecurityauth.security.userdetails

import br.com.ivan.springsecurityauth.model.User
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors



class UserDetailsImpl(
    private val user: User,
) : UserDetails {

    override fun getAuthorities(): MutableList<SimpleGrantedAuthority> = user.roles.stream()
        .map { role -> SimpleGrantedAuthority(role.name.name)
    }.collect(Collectors.toList())


    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}

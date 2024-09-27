package br.com.ivan.springsecurityauth.security.config

import br.com.ivan.springsecurityauth.security.authentication.UserAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Autowired
    private lateinit var userAuthenticationFilter: UserAuthenticationFilter



    @Autowired
    private lateinit var unauthorizedHandler: JwtAuthenticationEntryPoint


    companion object {
        val ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = arrayOf(
            "/users/login", //url que usaremos para fazer login
            "/users" //url que usaremos para criar um usuário
        )
        // Endpoints que requerem autenticação para serem acessados
        val ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = arrayOf(
            "/users/test",
            "/users/refresh-token"
        )
        // Endpoints que só podem ser acessador por usuários com permissão de cliente
        val ENDPOINTS_CUSTOMER = arrayOf(
            "/users/test/customer"
        )
        // Endpoints que só podem ser acessador por usuários com permissão de administrador
        val ENDPOINTS_ADMIN = arrayOf(
            "/users/test/administrator"
        )
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configura a política de criação de sessão como stateless
            }
            .authorizeHttpRequests{authorize ->
                authorize
                    .requestMatchers(*ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                    .requestMatchers(*ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated()
                    .requestMatchers(*ENDPOINTS_ADMIN).hasRole("ADMINISTRATOR")
                    .requestMatchers(*ENDPOINTS_CUSTOMER).hasRole("CUSTOMER")
                    .anyRequest().denyAll()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(unauthorizedHandler)
            }
            .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()





}

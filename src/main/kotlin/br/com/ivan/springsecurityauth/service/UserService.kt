package br.com.ivan.springsecurityauth.service

import br.com.ivan.springsecurityauth.dto.CreateUserDto
import br.com.ivan.springsecurityauth.dto.LoginUserDto
import br.com.ivan.springsecurityauth.dto.RecoveryJwtTokenDto
import br.com.ivan.springsecurityauth.mapper.RecoveryUserDtoMapper
import br.com.ivan.springsecurityauth.mapper.UserMapper
import br.com.ivan.springsecurityauth.repository.UserRepository
import br.com.ivan.springsecurityauth.security.authentication.JwtTokenService
import br.com.ivan.springsecurityauth.security.config.SecurityConfiguration
import br.com.ivan.springsecurityauth.security.userdetails.UserDetailsImpl
import com.auth0.jwt.exceptions.JWTVerificationException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val recoveryUserDtoMapper: RecoveryUserDtoMapper,
    private val userMapper: UserMapper
) {

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var jwtTokenService: JwtTokenService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var securityConfiguration: SecurityConfiguration

    //@Value("\${auth.jwt.token.expiration}")
    private val horaExpiracaoToken = 40

    //@Value("\${auth.jwt.refresh-token.expiration}")
    private val horaExpiracaoRefreshToken = 3600

    // Método responsável por autenticar um usuário e retornar um token JWT
    fun authenticateUser(loginUserDto: LoginUserDto): RecoveryJwtTokenDto{
        // Cria um objeto de autenticação com o email e a senha do usuário
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(loginUserDto.email, loginUserDto.password)

        // Autentica o usuário com as credenciais fornecidas
        val authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken)

        // Obtém o objeto UserDetails do usuário autenticado
        val userDetails = authentication.principal as UserDetailsImpl

        val user = userRepository.findByEmail(userDetails.username).orElseThrow {
            throw NotFoundException()
        }

        val recoveryUserDto = recoveryUserDtoMapper.map(user)

        // Gera um token JWT para o usuário autenticado
        return RecoveryJwtTokenDto(
            token = jwtTokenService.generateToken(userDetails, horaExpiracaoToken),
            refreshToken = jwtTokenService.generateToken(userDetails, horaExpiracaoRefreshToken),
            user = recoveryUserDto)
    }


    // Método para obter novo toke usando o refresh token
    fun obterRefreshToken(
        request: HttpServletRequest
    ): RecoveryJwtTokenDto{

        val token = recoveryToken(request)
        val subject = jwtTokenService.getSubjectFromToken(token.toString())

        val user = userRepository.findByEmail(subject).orElseThrow {
            throw JWTVerificationException("Refresh Token inválido ou expirado.")
        }
        val userDetails = UserDetailsImpl(user)
        val autentication = UsernamePasswordAuthenticationToken(userDetails.username, null, userDetails.authorities)

        SecurityContextHolder.getContext().authentication = autentication

        return RecoveryJwtTokenDto(
            token = jwtTokenService.generateToken(userDetails, horaExpiracaoToken),
            refreshToken = jwtTokenService.generateToken(userDetails, horaExpiracaoRefreshToken)
        )
    }



    // Método responsável por criar um usuário
    fun createUser(createUserDto: CreateUserDto){
        // Cria um novo usuário com os dados fornecidos
        val newUser = userMapper.map(createUserDto)
        // Salva o novo usuário no banco de dados
        userRepository.save(newUser)
    }


    // Recupera o token do cabeçalho Authorization da requisição
    private fun recoveryToken(request: HttpServletRequest): String? {
        val authorizationHeader = request.getHeader("Authorization")
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "")
        }
        return null
    }






}

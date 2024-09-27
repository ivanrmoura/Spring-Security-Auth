package br.com.ivan.springsecurityauth.security.authentication


import br.com.ivan.springsecurityauth.repository.UserRepository
import br.com.ivan.springsecurityauth.security.userdetails.UserDetailsImpl
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


@Service
class JwtTokenService {

    // Chave secreta utilizada para gerar e verificar o token
    private val SECRET_KEY: String = "4Z^XrroxR@dWxqf\$mTTKwW$!@#qGr4P"

    private val ISSUER: String = "security-api"

    //@Value("\${auth.jwt.token.expiration}")
    private val horaExpiracaoToken = 1

    //@Value("\${auth.jwt.refresh-token.expiration}")
    private val horaExpiracaoRefreshToken = 8

    @Autowired
    private lateinit var userRepository: UserRepository

    fun generateToken(user: UserDetailsImpl, expiration: Int): String{
        try {
            val algorithm = Algorithm.HMAC256(SECRET_KEY)
            return JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(creationDate())
                .withExpiresAt(geraDataExpiracao(expiration))
                .withSubject(user.username)
                .sign(algorithm)
        }catch (exception: JWTCreationException){
            throw JWTCreationException("Erro ao gerar token", exception)
        }
    }

    fun getSubjectFromToken(token: String): String{
        try {
            val algorithm = Algorithm.HMAC256(SECRET_KEY)
            return JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build()
                .verify(token)
                .subject
        }catch (exception: JWTVerificationException){
            throw JWTVerificationException("Token inválido ou expirado.")
        }
    }



    private fun geraDataExpiracao(expiration: Int): Instant {
        return ZonedDateTime.now(ZoneId.of("America/Recife"))
            .plusSeconds(expiration.toLong()).toInstant()
    }

   private fun creationDate(): Instant {
        return ZonedDateTime.now(ZoneId.of("America/Recife")).toInstant()
    }

    /*
    private fun expirationDate(): Instant {
        return ZonedDateTime.now(ZoneId.of("America/Recife")).plusHours(4).toInstant()
    }*/

}

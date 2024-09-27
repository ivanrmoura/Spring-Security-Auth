package br.com.ivan.springsecurityauth.security.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response!!.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val message: String

        if (authException?.cause != null) {
            message = authException.cause.toString() + " " + authException.message
        } else {
            message = authException?.message!!
        }

        val body = ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message))

        response.outputStream.write(body)
    }
}

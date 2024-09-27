package br.com.ivan.springsecurityauth.controller

import br.com.ivan.springsecurityauth.dto.CreateUserDto
import br.com.ivan.springsecurityauth.dto.LoginUserDto
import br.com.ivan.springsecurityauth.dto.RecoveryJwtTokenDto
import br.com.ivan.springsecurityauth.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("users")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/login")
    fun authenticateUser(@RequestBody loginUserDto: LoginUserDto
    ): ResponseEntity<RecoveryJwtTokenDto> {
        val token = userService.authenticateUser(loginUserDto)
        return ResponseEntity(token, HttpStatus.OK)
    }

    @PostMapping("/refresh-token")
    fun authRefreshToken(
        request: HttpServletRequest
    ): ResponseEntity<RecoveryJwtTokenDto> {
        val token = userService.obterRefreshToken(request)
        return ResponseEntity(token, HttpStatus.OK)
    }


    @PostMapping
    fun createUser(
        @RequestBody createUserDto: CreateUserDto
    ): ResponseEntity<Void>{
        userService.createUser(createUserDto)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping("/test")
    fun getAuthenticationTest(): ResponseEntity<String>{
        return ResponseEntity("Autenticado com sucesso", HttpStatus.OK)
    }

    @GetMapping("/test/customer")
    fun getCustomerAuthenticationTest(): ResponseEntity<String> {
        return ResponseEntity("Cliente autenticado com sucesso", HttpStatus.OK)
    }

    @GetMapping("/test/administrator")
    fun getAdminAuthenticationTest(): ResponseEntity<String> {
        return ResponseEntity("Administrador autenticado com sucesso", HttpStatus.OK)
    }


}

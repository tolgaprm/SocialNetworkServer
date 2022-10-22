package com.prmcoding.plugins

import com.prmcoding.data.repository.user.UserRepository
import com.prmcoding.routes.user.createUserRoute
import com.prmcoding.routes.user.loginUserRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    routing {
        createUserRoute(userRepository = userRepository)
        loginUserRoute(userRepository = userRepository)
    }
}

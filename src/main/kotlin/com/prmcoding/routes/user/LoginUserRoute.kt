package com.prmcoding.routes.user

import com.prmcoding.data.requests.LoginRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.service.UserService
import com.prmcoding.util.ApiResponseMessages.INVALID_CREDENTIALS
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.loginUserRoute(userService: UserService) {

    post("/api/user/login") {
        val request = kotlin.runCatching { call.receiveNullable<LoginRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (userService.validateLoginRequest(request = request) == UserService.ValidationEvent.ErrorFieldEmpty) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val isCorrectPassword = userService.doesPasswordForUserMatch(request = request)

        if (isCorrectPassword) {
            call.respond(
                status = HttpStatusCode.OK,
                message = BasicApiResponse(
                    successful = true
                )
            )
        } else {
            call.respond(
                status = HttpStatusCode.OK,
                message = BasicApiResponse(
                    successful = false,
                    message = INVALID_CREDENTIALS
                )
            )
        }
    }

}
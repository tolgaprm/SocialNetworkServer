package com.prmcoding.routes.user

import com.prmcoding.data.requests.CreateAccountRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.service.UserService
import com.prmcoding.util.ApiResponseMessages.FIELDS_BLANK
import com.prmcoding.util.ApiResponseMessages.USER_ALREADY_EXISTS
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createUser(userService: UserService) {
    post("/api/user/create") {

        val request = kotlin.runCatching { call.receiveNullable<CreateAccountRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (userService.doesUserWithEmailExist(email = request.email)) {
            call.respond(
                BasicApiResponse<Unit>(
                    message = USER_ALREADY_EXISTS,
                    successful = false
                )
            )
            return@post
        }

        when (userService.validateCreateAccountRequest(request = request)) {
            is UserService.ValidationEvent.ErrorFieldEmpty -> {
                call.respond(
                    BasicApiResponse<Unit>(
                        message = FIELDS_BLANK,
                        successful = false
                    )
                )
            }
            is UserService.ValidationEvent.Success -> {
                userService.createUser(request = request)
                call.respond(
                    BasicApiResponse<Unit>(successful = true)
                )
            }
        }
    }
}
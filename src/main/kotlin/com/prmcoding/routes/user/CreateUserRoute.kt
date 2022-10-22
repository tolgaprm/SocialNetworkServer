package com.prmcoding.routes.user

import com.prmcoding.data.models.User
import com.prmcoding.data.repository.user.UserRepository
import com.prmcoding.data.requests.CreateAccountRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.util.ApiResponseMessages.FIELDS_BLANK
import com.prmcoding.util.ApiResponseMessages.USER_ALREADY_EXISTS
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createUserRoute(userRepository: UserRepository) {
    post("/api/user/create") {

        val request = kotlin.runCatching { call.receiveNullable<CreateAccountRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val userExists = userRepository.getUserByEmail(email = request.email) != null
        if (userExists) {
            call.respond(
                BasicApiResponse(
                    message = USER_ALREADY_EXISTS,
                    successful = false
                )
            )
            return@post
        }

        if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
            call.respond(
                BasicApiResponse(
                    message = FIELDS_BLANK,
                    successful = false
                )
            )
            return@post
        }


        userRepository.createUser(
            User(
                email = request.email,
                username = request.username,
                password = request.password,
                profileImageUrl = "",
                bio = "",
                gitHubUrl = "",
                instagramUrl = "",
                linkedInUrl = ""
            )
        )
        call.respond(
            BasicApiResponse(successful = true)
        )

    }

}
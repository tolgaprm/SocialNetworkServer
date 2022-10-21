package com.prmcoding.routes

import com.prmcoding.controller.user.UserController
import com.prmcoding.data.models.User
import com.prmcoding.data.requests.CreateAccountRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.util.ApiResponseMessages.FIELDS_BLANK
import com.prmcoding.util.ApiResponseMessages.USER_ALREADY_EXISTS
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val userController: UserController by inject()
    route("/api/user/create") {
        post {
            val request = call.receiveNullable<CreateAccountRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val userExists = userController.getUserByEmail(email = request.email) != null
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


            userController.createUser(
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
}
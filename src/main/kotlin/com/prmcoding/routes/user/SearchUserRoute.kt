package com.prmcoding.routes.user

import com.prmcoding.responses.UserResponseItem
import com.prmcoding.routes.userId
import com.prmcoding.service.UserService
import com.prmcoding.util.QueryParameters
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.searchUserRoute(
    userService: UserService
) {
    authenticate {
        get("/api/user/search") {
            val query = call.parameters[QueryParameters.PARAM_QUERY]

            if (query == null || query.isBlank()) {
                call.respond(
                    HttpStatusCode.OK,
                    listOf<UserResponseItem>()
                )
                return@get
            }

            val searchResult = userService.searchForUsers(
                query = query,
                userId = call.userId
            )

            call.respond(
                HttpStatusCode.OK,
                searchResult
            )
        }
    }
}
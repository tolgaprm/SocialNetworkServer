package com.prmcoding.routes.likes

import com.prmcoding.data.requests.LikeUpdateRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.routes.userId
import com.prmcoding.service.LikeService
import com.prmcoding.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.likeParentRoute(
    likeService: LikeService

) {
    authenticate {
        post("/api/like") {
            val request = kotlin.runCatching { call.receiveNullable<LikeUpdateRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val likeSuccessful = likeService.likeParent(
                userId = call.userId,
                parentId = request.parentId
            )
            if (likeSuccessful) {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse(
                        successful = true,
                    )
                )
            } else {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }

        }
    }
}
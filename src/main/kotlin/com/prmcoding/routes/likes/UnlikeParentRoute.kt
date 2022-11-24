package com.prmcoding.routes.likes

import com.prmcoding.data.requests.LikeUpdateRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.service.LikeService
import com.prmcoding.service.UserService
import com.prmcoding.util.ApiResponseMessages
import com.prmcoding.util.ifEmailBelongToUserId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unlikeParentRoute(
    likeService: LikeService,
    userService: UserService
) {
    authenticate {
        delete("/api/unlike") {
            val request = kotlin.runCatching { call.receiveNullable<LikeUpdateRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            ifEmailBelongToUserId(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongsToUserId
            ) {
                val unlikeSuccessful = likeService.unLikeParent(
                    userId = request.userId,
                    parentId = request.parentId
                )
                if (unlikeSuccessful) {
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
}
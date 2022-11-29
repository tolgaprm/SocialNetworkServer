package com.prmcoding.routes.follow

import com.prmcoding.data.requests.FollowUpdateRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.routes.userId
import com.prmcoding.service.FollowService
import com.prmcoding.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unFollowUserRoute(
    followService: FollowService
) {
    authenticate {
        delete("/api/following/unfollow") {
            val request = kotlin.runCatching { call.receiveNullable<FollowUpdateRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val didUserExist = followService.unFollowUserIfExists(
                followingUserId = call.userId,
                followedUserId = request.followedUserId
            )

            if (didUserExist) {
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
                        successful = true,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }
        }
    }
}
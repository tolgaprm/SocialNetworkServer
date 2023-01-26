package com.prmcoding.routes.follow

import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.routes.userId
import com.prmcoding.service.FollowService
import com.prmcoding.util.ApiResponseMessages
import com.prmcoding.util.QueryParameters
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unFollowUser(
    followService: FollowService
) {
    authenticate {
        delete("/api/following/unfollow") {
            val followedUserId = call.parameters[QueryParameters.PARAM_USER_ID] ?: kotlin.run {
                BasicApiResponse<Unit>(
                    successful = false
                )
                return@delete
            }
            val didUserExist = followService.unFollowUserIfExists(
                followingUserId = call.userId,
                followedUserId = followedUserId
            )

            if (didUserExist) {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse<Unit>(
                        successful = true,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }
        }
    }
}
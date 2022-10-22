package com.prmcoding.routes.follow

import com.prmcoding.data.repository.follow.FollowRepository
import com.prmcoding.data.requests.FollowUpdateRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.util.ApiResponseMessages.USER_NOT_FOUND
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.followUserRoute(followRepository: FollowRepository) {

    post("/api/following/follow") {
        val request = kotlin.runCatching { call.receiveNullable<FollowUpdateRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

            val didUserExist =  followRepository.followUserIfExists(
                followingUserId = request.followingUserId,
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
                        message = USER_NOT_FOUND
                    )
                )
            }




    }
}
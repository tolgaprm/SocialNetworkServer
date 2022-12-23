package com.prmcoding.routes.follow

import com.prmcoding.data.models.Activity
import com.prmcoding.data.requests.FollowUpdateRequest
import com.prmcoding.data.util.ActivityType
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.routes.userId
import com.prmcoding.service.ActivityService
import com.prmcoding.service.FollowService
import com.prmcoding.util.ApiResponseMessages.USER_NOT_FOUND
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.followUser(
    followService: FollowService,
    activityService: ActivityService
) {

    authenticate {

        post("/api/following/follow") {
            val request = kotlin.runCatching { call.receiveNullable<FollowUpdateRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val userId = call.userId
            val didUserExist = followService.followUserIfExists(
                followingUserId = userId,
                followedUserId = request.followedUserId
            )
            if (didUserExist) {
                activityService.createActivity(
                    Activity(
                        timestamp = System.currentTimeMillis(),
                        byUserId = userId,
                        toUserId = request.followedUserId,
                        type = ActivityType.FollowedUser.type,
                        parentId = ""
                    )
                )
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
                        message = USER_NOT_FOUND
                    )
                )
            }
        }
    }
}
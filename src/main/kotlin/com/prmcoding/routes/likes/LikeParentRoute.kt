package com.prmcoding.routes.likes

import com.prmcoding.data.requests.LikeUpdateRequest
import com.prmcoding.data.util.ParentType
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.routes.userId
import com.prmcoding.service.ActivityService
import com.prmcoding.service.LikeService
import com.prmcoding.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.likeParentRoute(
    likeService: LikeService,
    activityService: ActivityService
) {
    authenticate {
        post("/api/like") {
            val request = kotlin.runCatching { call.receiveNullable<LikeUpdateRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }


            val userId = call.userId

            val likeSuccessful = likeService.likeParent(
                userId = userId,
                parentId = request.parentId,
                parentType = request.parentType
            )
            if (likeSuccessful) {
                activityService.addLikeActivity(
                    byUserId = userId,
                    parentType = ParentType.fromType(request.parentType),
                    parentId = request.parentId
                )
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
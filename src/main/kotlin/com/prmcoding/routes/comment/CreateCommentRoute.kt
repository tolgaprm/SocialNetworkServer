package com.prmcoding.routes.comment

import com.prmcoding.data.requests.CreateCommentRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.routes.userId
import com.prmcoding.service.ActivityService
import com.prmcoding.service.CommentService
import com.prmcoding.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createComment(
    commentService: CommentService,
    activityService: ActivityService
) {
    authenticate {
        post("/api/comment/create") {
            val request =
                kotlin.runCatching { call.receiveNullable<CreateCommentRequest>() }.getOrNull() ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }


            val userId = call.userId

            when (commentService.createComment(
                createCommentRequest = request,
                userId = userId
            )) {
                is CommentService.ValidationEvent.ErrorFieldEmpty -> {
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.FIELDS_BLANK
                        )
                    )
                }

                is CommentService.ValidationEvent.ErrorCommentTooLong -> {
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.COMMENT_TOO_LONG
                        )
                    )
                }

                is CommentService.ValidationEvent.Success -> {
                    activityService.addCommentActivity(
                        byUserId = userId,
                        postId = request.postId
                    )
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                }

                is CommentService.ValidationEvent.UserNotFound -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = "User not found"
                        )
                    )
                }
            }
        }
    }
}
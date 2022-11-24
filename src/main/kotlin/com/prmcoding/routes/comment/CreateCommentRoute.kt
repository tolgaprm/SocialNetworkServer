package com.prmcoding.routes.comment

import com.prmcoding.data.requests.CreateCommentRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.routes.ifEmailBelongToUserId
import com.prmcoding.service.CommentService
import com.prmcoding.service.UserService
import com.prmcoding.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createCommentRoute(
    commentService: CommentService,
    userService: UserService
) {
    authenticate {
        post("/api/comment/create") {
            val request =
                kotlin.runCatching { call.receiveNullable<CreateCommentRequest>() }.getOrNull() ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

            ifEmailBelongToUserId(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongsToUserId
            ) {
                when (commentService.createComment(request)) {
                    is CommentService.ValidationEvent.ErrorFieldEmpty -> {
                        call.respond(
                            status = HttpStatusCode.OK,
                            message = BasicApiResponse(
                                successful = false,
                                message = ApiResponseMessages.FIELDS_BLANK
                            )
                        )
                    }
                    is CommentService.ValidationEvent.ErrorCommentTooLong -> {
                        call.respond(
                            status = HttpStatusCode.OK,
                            message = BasicApiResponse(
                                successful = false,
                                message = ApiResponseMessages.COMMENT_TOO_LONG
                            )
                        )
                    }
                    is CommentService.ValidationEvent.Success -> {
                        call.respond(
                            status = HttpStatusCode.OK,
                            message = BasicApiResponse(
                                successful = true
                            )
                        )
                    }
                }
            }
        }
    }
}
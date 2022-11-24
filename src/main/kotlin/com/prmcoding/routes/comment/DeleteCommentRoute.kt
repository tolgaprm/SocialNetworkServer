package com.prmcoding.routes.comment

import com.prmcoding.data.requests.DeleteCommentRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.routes.ifEmailBelongToUserId
import com.prmcoding.service.CommentService
import com.prmcoding.service.LikeService
import com.prmcoding.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteCommentRoute(
    commentService: CommentService,
    userService: UserService,
    likeService: LikeService
) {
    authenticate {
        delete("/api/comment/delete") {
            val request =
                kotlin.runCatching { call.receiveNullable<DeleteCommentRequest>() }.getOrNull() ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }

            ifEmailBelongToUserId(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongsToUserId
            ) {
                val isDeletedComment = commentService.deleteComment(commentId = request.commentId)
                if (isDeletedComment) {
                    likeService.deleteLikesForParent(parentId = request.commentId)
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = BasicApiResponse(
                            successful = true
                        )
                    )
                } else {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        message = BasicApiResponse(
                            successful = false
                        )
                    )
                }
            }
        }
    }
}

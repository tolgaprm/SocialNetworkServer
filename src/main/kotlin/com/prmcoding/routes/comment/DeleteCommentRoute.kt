package com.prmcoding.routes.comment

import com.prmcoding.data.requests.DeleteCommentRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.routes.userId
import com.prmcoding.service.CommentService
import com.prmcoding.service.LikeService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteCommentRoute(
    commentService: CommentService,
    likeService: LikeService
) {
    authenticate {
        delete("/api/comment/delete") {
            val request =
                kotlin.runCatching { call.receiveNullable<DeleteCommentRequest>() }.getOrNull() ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }


            val comment = commentService.getCommentById(commentId = request.commentId)

            if (comment?.userId != call.userId) {
                call.respond(HttpStatusCode.Unauthorized)
                return@delete
            }

            val isDeletedComment = commentService.deleteComment(commentId = request.commentId)
            if (isDeletedComment) {
                likeService.deleteLikesForParent(parentId = request.commentId)
                call.respond(HttpStatusCode.OK, BasicApiResponse(successful = true))
            } else {
                call.respond(HttpStatusCode.NotFound, BasicApiResponse(successful = false))
            }
        }
    }
}

package com.prmcoding.routes.post

import com.prmcoding.data.requests.DeletePostRequest
import com.prmcoding.routes.userId
import com.prmcoding.service.CommentService
import com.prmcoding.service.LikeService
import com.prmcoding.service.PostService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deletePostRoute(
    postService: PostService,
    likeService: LikeService,
    commentService: CommentService
) {
    authenticate {
        delete("/api/post/delete") {
            val request = kotlin.runCatching { call.receiveNullable<DeletePostRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val post = postService.getPost(postId = request.postId)
            if (post == null) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }

            if (post.userId == call.userId) {
                postService.deletePost(postId = request.postId)
                likeService.deleteLikesForParent(parentId = request.postId)
                commentService.deleteCommentsForParent(parentId = request.postId)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }

        }
    }
}
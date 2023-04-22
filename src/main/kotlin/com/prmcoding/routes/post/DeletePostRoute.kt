package com.prmcoding.routes.post

import com.prmcoding.routes.userId
import com.prmcoding.service.CommentService
import com.prmcoding.service.LikeService
import com.prmcoding.service.PostService
import com.prmcoding.util.QueryParameters
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deletePost(
    postService: PostService,
    likeService: LikeService,
    commentService: CommentService
) {
    authenticate {
        delete("/api/post/delete") {
            val postId = call.parameters[QueryParameters.PARAM_POST_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val post = postService.getPost(postId = postId)
            if (post == null) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }

            if (post.userId == call.userId) {
                postService.deletePost(postId = postId)
                likeService.deleteLikesForParent(parentId = postId)
                commentService.deleteCommentsForParent(parentId = postId)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }

        }
    }
}
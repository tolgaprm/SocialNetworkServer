package com.prmcoding.routes.post

import com.prmcoding.data.requests.DeletePostRequest
import com.prmcoding.service.LikeService
import com.prmcoding.service.PostService
import com.prmcoding.service.UserService
import com.prmcoding.util.ifEmailBelongToUserId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deletePostRoute(
    postService: PostService,
    userService: UserService,
    likeService: LikeService
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

            ifEmailBelongToUserId(
                userId = post.userId,
                validateEmail = userService::doesEmailBelongsToUserId
            ) {
                postService.deletePost(postId = request.postId)
                likeService.deleteLikesForParent(parentId = request.postId)
                // TODO: Delete comments from post
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
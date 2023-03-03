package com.prmcoding.routes.post

import com.prmcoding.routes.userId
import com.prmcoding.service.PostService
import com.prmcoding.util.Constants
import com.prmcoding.util.QueryParameters
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getPostsForProfile(
    postService: PostService
) {
    authenticate {
        get("/api/user/posts") {
            val userId = call.parameters[QueryParameters.PARAM_USER_ID]
            val page = call.parameters[QueryParameters.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize =
                call.parameters[QueryParameters.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_POST_PAGE_SIZE

            val posts = postService.getPostsForProfile(
                ownUserId = call.userId,
                userId = userId ?: call.userId,
                page = page,
                pageSize = pageSize
            )

            call.respond(
                status = HttpStatusCode.OK,
                message = posts
            )
        }
    }
}
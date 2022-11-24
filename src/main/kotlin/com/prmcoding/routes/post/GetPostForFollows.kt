package com.prmcoding.routes.post

import com.prmcoding.service.PostService
import com.prmcoding.service.UserService
import com.prmcoding.util.Constants
import com.prmcoding.util.QueryParameters
import com.prmcoding.util.ifEmailBelongToUserId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getPostForFollows(
    postService: PostService,
    userService: UserService
) {
    authenticate {
        get("/api/post/get") {
            val userId = call.parameters[QueryParameters.PARAM_USER_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val page = call.parameters[QueryParameters.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize =
                call.parameters[QueryParameters.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_POST_PAGE_SIZE

            ifEmailBelongToUserId(
                userId = userId,
                validateEmail = userService::doesEmailBelongsToUserId
            ) {
                val posts = postService.getPostsForFollows(
                    userId = userId,
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
}
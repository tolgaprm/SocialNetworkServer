package com.prmcoding.routes.post

import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.routes.userId
import com.prmcoding.service.PostService
import com.prmcoding.util.QueryParameters.PARAM_POST_ID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getPostDetails(
    postService: PostService
) {
    get("/api/post/details") {
        val postId = call.parameters[PARAM_POST_ID] ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }

        val post = postService.getPostDetails(userId = call.userId, postId = postId) ?: kotlin.run {
            call.respond(
                HttpStatusCode.NotFound
            )
            return@get
        }

        call.respond(
            HttpStatusCode.OK,
            BasicApiResponse(
                successful = true,
                data = post
            )
        )
    }
}
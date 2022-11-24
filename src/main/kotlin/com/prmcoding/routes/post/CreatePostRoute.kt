package com.prmcoding.routes.post

import com.prmcoding.data.requests.CreatePostRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.service.PostService
import com.prmcoding.service.UserService
import com.prmcoding.util.ApiResponseMessages.USER_NOT_FOUND
import com.prmcoding.util.ifEmailBelongToUserId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createPostRoute(
    postService: PostService,
    userService: UserService
) {

    authenticate {
        post("/api/post/create") {
            val request = kotlin.runCatching { call.receiveNullable<CreatePostRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }


            ifEmailBelongToUserId(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongsToUserId
            ) {
                val didUserExists = postService.createPostIfUserExists(request = request)
                if (didUserExists) {
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = BasicApiResponse(
                            successful = true
                        )
                    )
                } else {
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = BasicApiResponse(
                            successful = true,
                            message = USER_NOT_FOUND
                        )
                    )
                }
            }
        }
    }
}
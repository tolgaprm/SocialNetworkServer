package com.prmcoding.routes.post

import com.prmcoding.data.requests.CreatePostRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.routes.userId
import com.prmcoding.service.PostService
import com.prmcoding.util.Constants
import com.prmcoding.util.Constants.POST_PICTURE_PATH
import com.prmcoding.util.save
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

fun Route.createPost(
    postService: PostService
) {

    authenticate {
        post("/api/post/create") {
            val userId = call.userId
            val multipart = call.receiveMultipart()
            var createPostRequest: CreatePostRequest? = null
            var fileName: String? = null
            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "post_data") {
                            createPostRequest =
                                Json.decodeFromString<CreatePostRequest>(partData.value)
                        }
                    }

                    is PartData.FileItem -> {
                        fileName = partData.save(POST_PICTURE_PATH)
                    }

                    is PartData.BinaryItem -> Unit
                    is PartData.BinaryChannelItem -> Unit
                }
            }
            val postPictureUrl = "${Constants.BASE_URL}post_pictures/$fileName"
            createPostRequest?.let { request ->
                val createPostAcknowledged = postService.createPost(
                    request = request,
                    userId = userId,
                    imageUrl = postPictureUrl
                )
                if (createPostAcknowledged) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(successful = true)
                    )
                } else {
                    File("$POST_PICTURE_PATH$fileName").delete()
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        }
    }
}
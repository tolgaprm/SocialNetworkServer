package com.prmcoding.routes.user

import com.prmcoding.data.requests.UpdateProfileRequest
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.routes.userId
import com.prmcoding.service.UserService
import com.prmcoding.util.Constants.BASE_URL
import com.prmcoding.util.Constants.PROFILE_PICTURE_PATH
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
import java.util.*


fun Route.updateUserProfile(
    userService: UserService
) {
    authenticate {
        put("/api/user/update") {
            //Multipart request -> İçerisinde byte dataları içeren isteklerdir.
            val multipart = call.receiveMultipart()
            var updateProfileRequest: UpdateProfileRequest? = null
            var fileName: String? = null
            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "update_profile_data") {
                            updateProfileRequest =
                                Json.decodeFromString<UpdateProfileRequest>(partData.value)
                        }
                    }

                    is PartData.FileItem -> {
                        val fileBytes = partData.streamProvider().readBytes()
                        val fileExtension = partData.originalFileName?.takeLastWhile { it != '.' }
                        fileName = UUID.randomUUID().toString() + "." + fileExtension
                        File("$PROFILE_PICTURE_PATH$fileName").writeBytes(fileBytes)
                    }

                    is PartData.BinaryItem -> Unit
                    is PartData.BinaryChannelItem -> Unit
                }
            }
            val profilePictureUrl = "${BASE_URL}profile_pictures/$fileName"
            updateProfileRequest?.let { request ->
                val updateAcknowledged = userService.updateUser(
                    userId = call.userId,
                    profileImageUrl = profilePictureUrl,
                    updateProfileRequest = request
                )
                if (updateAcknowledged) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(successful = true)
                    )
                } else {
                    File("$PROFILE_PICTURE_PATH$fileName").delete()
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
        }
    }
}
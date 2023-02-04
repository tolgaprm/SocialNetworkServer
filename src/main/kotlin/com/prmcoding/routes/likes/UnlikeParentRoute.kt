package com.prmcoding.routes.likes

import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.routes.userId
import com.prmcoding.service.LikeService
import com.prmcoding.util.ApiResponseMessages
import com.prmcoding.util.QueryParameters
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unlikeParent(
    likeService: LikeService
) {
    authenticate {
        delete("/api/unlike") {
            val parentId = call.parameters[QueryParameters.PARAM_PARENT_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val parentType = call.parameters[QueryParameters.PARAM_PARENT_TYPE]?.toIntOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }


            val unlikeSuccessful = likeService.unLikeParent(
                userId = call.userId,
                parentId = parentId,
                parentType = parentType
            )
            if (unlikeSuccessful) {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse<Unit>(
                        successful = true,
                    )
                )
            } else {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }

        }
    }
}
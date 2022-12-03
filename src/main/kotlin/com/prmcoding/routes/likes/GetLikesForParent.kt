package com.prmcoding.routes.likes

import com.prmcoding.routes.userId
import com.prmcoding.service.LikeService
import com.prmcoding.util.QueryParameters
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getLikesForParent(
    likeService: LikeService
) {
    authenticate {
        get("/api/like/parent") {
            val parentId = call.parameters[QueryParameters.PARAM_PARENT_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val page = call.parameters[QueryParameters.PARAM_PAGE]?.toIntOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val pageSize = call.parameters[QueryParameters.PARAM_PAGE_SIZE]?.toIntOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val usersWhoLikedParent = likeService.getUsersWhoLikedParent(
                parentId = parentId,
                page = page,
                pageSize = pageSize,
                userId = call.userId
            )
            call.respond(
                HttpStatusCode.OK,
                usersWhoLikedParent
            )
        }
    }
}
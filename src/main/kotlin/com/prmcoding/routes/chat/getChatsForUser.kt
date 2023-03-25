package com.prmcoding.routes.chat

import com.prmcoding.routes.userId
import com.prmcoding.service.chat.ChatService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getChatsForUser(chatService: ChatService) {
    authenticate {
        get("/api/chats") {
            val chats = chatService.getChatsForUser(ownUserId = call.userId)
            call.respond(
                status = HttpStatusCode.OK,
                message = chats
            )
        }
    }
}
package com.prmcoding.routes.chat

import com.prmcoding.routes.userId
import com.prmcoding.service.chat.ChatService
import com.prmcoding.util.Constants
import com.prmcoding.util.QueryParameters
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getMessagesForChat(chatService: ChatService) {
    authenticate {
        get("/api/chat/messages") {
            val chatId = call.parameters[QueryParameters.PARAM_CHAT_ID] ?: kotlin.run {
                call.respond(message = HttpStatusCode.BadRequest)
                return@get
            }
            val page = call.parameters[QueryParameters.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize =
                call.parameters[QueryParameters.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_SIZE

            if (!chatService.doesChatBelongToUser(chatId = chatId, userId = call.userId)) {
                call.respond(message = HttpStatusCode.Forbidden)
                return@get
            }

            val messages = chatService.getMessagesForChat(
                chatId = chatId,
                page = page,
                pageSize = pageSize
            )

            call.respond(
                status = HttpStatusCode.OK,
                message = messages
            )
        }
    }
}
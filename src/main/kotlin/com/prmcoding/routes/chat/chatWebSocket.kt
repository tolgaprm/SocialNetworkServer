package com.prmcoding.routes.chat

import com.prmcoding.data.websocket.WSServerMessage
import com.prmcoding.service.chat.ChatController
import com.prmcoding.service.chat.ChatSession
import com.prmcoding.util.WebSocketObject
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.chatWebSocket(
    chatController: ChatController
) {
    webSocket("/api/chat/websocket") {
        val session = call.sessions.get<ChatSession>()

        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
            return@webSocket
        }
        chatController.onJoin(chatSession = session, socket = this)

        try {
            incoming.consumeEach { frame ->
                kotlin.run {
                    when (frame) {
                        is Frame.Text -> {
                            val frameText = frame.readText()
                            val delimitersIndex = frameText.indexOf("#")
                            if (delimitersIndex == -1) {
                                return@run
                            }
                            val type = frameText.substring(0, delimitersIndex).toIntOrNull() ?: return@run
                            val json = frameText.substring(delimitersIndex + 1, frameText.length)

                            handleWebSocket(
                                webSocketSession = this,
                                session = session,
                                chatController = chatController,
                                frameText = frameText,
                                type = type,
                                json = json
                            )
                        }

                        else -> {}
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            chatController.onDisconnect(session.userId)
        }
    }
}

suspend fun handleWebSocket(
    webSocketSession: WebSocketSession,
    session: ChatSession,
    chatController: ChatController,
    frameText: String,
    type: Int,
    json: String
) {
    when (type) {
        WebSocketObject.MESSAGE.ordinal -> {
            val message = Json.decodeFromString<WSServerMessage>(json)
            chatController.sendMessage(frameText = frameText, message = message)
        }
    }
}
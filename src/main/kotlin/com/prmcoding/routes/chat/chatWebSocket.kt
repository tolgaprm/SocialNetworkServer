package com.prmcoding.routes.chat

import com.prmcoding.data.websocket.WSClientMessage
import com.prmcoding.routes.userId
import com.prmcoding.service.chat.ChatController
import com.prmcoding.util.WebSocketObject
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.chatWebSocket(
    chatController: ChatController
) {
    authenticate {
        webSocket("/api/chat/websocket") {
            chatController.onJoin(userId = call.userId, socket = this)
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
                                    ownUserId = call.userId,
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
                chatController.onDisconnect(call.userId)
            }
        }
    }
}

suspend fun handleWebSocket(
    ownUserId: String,
    chatController: ChatController,
    frameText: String,
    type: Int,
    json: String
) {
    when (type) {
        WebSocketObject.MESSAGE.ordinal -> {
            val message = Json.decodeFromString<WSClientMessage>(json)
            chatController.sendMessage(ownUserId = ownUserId, frameText = frameText, message = message)
        }
    }
}
package com.prmcoding.service.chat

import com.prmcoding.data.repository.chat.ChatRepository
import com.prmcoding.data.websocket.WSClientMessage
import com.prmcoding.data.websocket.WSServerMessage
import com.prmcoding.util.WebSocketObject
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class ChatController(
    private val repository: ChatRepository
) {
    private val onlineUsers = ConcurrentHashMap<String, WebSocketSession>()

    fun onJoin(userId: String, socket: WebSocketSession) {
        onlineUsers[userId] = socket
    }

    fun onDisconnect(userId: String) {
        if (onlineUsers.containsKey(userId)) {
            onlineUsers.remove(userId)
        }
    }

    suspend fun sendMessage(ownUserId: String, frameText: String, message: WSClientMessage) {
        val messageEntity = message.toMessage(ownUserId)
        val wsServerMessage = WSServerMessage(
            fromId = ownUserId,
            toId = message.toId,
            text = message.text,
            timestamp = System.currentTimeMillis(),
            chatId = message.chatId
        )
        val frameText = Json.encodeToString(wsServerMessage)
        onlineUsers[ownUserId]?.send(Frame.Text("${WebSocketObject.MESSAGE.ordinal}#$frameText"))
        onlineUsers[message.toId]?.send(Frame.Text("${WebSocketObject.MESSAGE.ordinal}#$frameText"))
        repository.insertMessage(messageEntity)
        if (!repository.doesChatByUsersExist(ownUserId, message.toId)) {
            repository.insertChat(ownUserId, message.toId, messageEntity.id)
        } else {
            message.chatId?.let {
                repository.updateLastMessageIdForChat(message.chatId, messageEntity.id)
            }
        }
    }
}
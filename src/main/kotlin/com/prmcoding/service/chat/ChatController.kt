package com.prmcoding.service.chat

import com.prmcoding.data.repository.message.ChatRepository
import com.prmcoding.data.websocket.WSMessage
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class ChatController(
    private val repository: ChatRepository
) {
    private val onlineUsers = ConcurrentHashMap<String, WebSocketSession>()

    fun onJoin(chatSession: ChatSession, socket: WebSocketSession) {
        onlineUsers[chatSession.userId] = socket
    }

    fun onDisconnect(userId: String) {
        if (onlineUsers.containsKey(userId)) {
            onlineUsers.remove(userId)
        }
    }

    suspend fun sendMessage(json: String, message: WSMessage) {
        onlineUsers[message.fromId]?.send(Frame.Text(json))
        onlineUsers[message.toId]?.send(Frame.Text(json))

        val messageEntity = message.toMessage()
        repository.insertMessage(messageEntity)

        if(!repository.doesChatByUsersExist(message.fromId, message.toId)) {
            repository.insertChat(message.fromId, message.toId, messageEntity.id)
        } else {
            message.chatId?.let {
                repository.updateLastMessageIdForChat(message.chatId, messageEntity.id)
            }
        }
    }
}
package com.prmcoding.service.chat

import com.prmcoding.data.models.Chat
import com.prmcoding.data.models.Message
import com.prmcoding.data.repository.chat.ChatRepository
import com.prmcoding.responses.ChatDto

class ChatService(
    private val chatRepository: ChatRepository
) {


    suspend fun doesChatBelongToUser(chatId: String, userId: String): Boolean {
        return chatRepository.doesChatBelongToUser(
            chatId = chatId,
            userId = userId
        )
    }

    suspend fun getMessagesForChat(
        chatId: String,
        page: Int,
        pageSize: Int
    ): List<Message> {
        return chatRepository.getMessagesForChat(
            chatId = chatId,
            page = page,
            pageSize = pageSize
        )
    }

    suspend fun getChatsForUser(ownUserId: String): List<ChatDto> {
        return chatRepository.getChatsForUsers(ownUserId = ownUserId)
    }
}
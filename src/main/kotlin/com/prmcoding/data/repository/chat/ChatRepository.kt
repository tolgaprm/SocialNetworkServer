package com.prmcoding.data.repository.chat

import com.prmcoding.data.models.Chat
import com.prmcoding.data.models.Message
import com.prmcoding.responses.ChatDto

interface ChatRepository {

    suspend fun getMessagesForChat(
        chatId: String,
        page: Int,
        pageSize: Int
    ): List<Message>

    suspend fun getChatsForUsers(ownUserId: String): List<ChatDto>

    suspend fun doesChatBelongToUser(
        chatId: String,
        userId: String
    ): Boolean

    suspend fun insertMessage(message: Message)

    suspend fun insertChat(userId1: String, userId2: String, messageId: String)

    suspend fun doesChatByUsersExist(userId1:String, userId2: String):Boolean

    suspend fun updateLastMessageIdForChat(chatId: String, lastMessageId: String)

}
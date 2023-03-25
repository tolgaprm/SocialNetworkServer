package com.prmcoding.data.repository.message

import com.prmcoding.data.models.Chat
import com.prmcoding.data.models.Message
import com.prmcoding.data.models.SimpleUser
import com.prmcoding.data.models.User
import kotlinx.coroutines.delay
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.insertOne

class ChatRepositoryImpl(
    private val db: CoroutineDatabase
) : ChatRepository {

    private val chats = db.getCollection<Chat>()
    private val users = db.getCollection<User>()
    private val messages = db.getCollection<Message>()
    override suspend fun getMessagesForChat(
        chatId: String,
        page: Int,
        pageSize: Int
    ): List<Message> {
        return messages.find(Message::chatId eq chatId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Message::timestamp)
            .toList()
    }

    override suspend fun getChatsForUsers(ownUserId: String): List<Chat> {
        val user = users.findOneById(ownUserId) ?: return emptyList()
        val simpleUser = SimpleUser(
            userId = user.id,
            profileImageUrl = user.profileImageUrl,
            username = user.username
        )
        return chats.find(Chat::userIds contains ownUserId)
            .descendingSort(Chat::timestamp)
            .toList()
    }

    override suspend fun doesChatBelongToUser(chatId: String, userId: String): Boolean {
        return chats.findOneById(chatId)?.userIds?.any { it == userId } == true
    }

    override suspend fun insertMessage(message: Message) {
        messages.insertOne(message)
    }


    override suspend fun insertChat(userId1: String, userId2: String, messageId: String) {
        val chat = Chat(
            userIds = listOf(
                userId1,
                userId2
            ),
            lastMessageId = messageId,
            timestamp = System.currentTimeMillis()
        )
        val chatId = chats.insertOne(chat).insertedId?.asObjectId().toString()
        messages.updateOneById(messageId, setValue(Message::chatId, chatId))
    }

    override suspend fun doesChatByUsersExist(userId1: String, userId2: String): Boolean {
        return chats.find(
            and(
                Chat::userIds contains userId1,
                Chat::userIds contains userId2
            )
        ).first() != null
    }

    override suspend fun updateLastMessageIdForChat(chatId: String, lastMessageId: String) {
        chats.updateOneById(chatId, setValue(Chat::lastMessageId, lastMessageId))
    }


}
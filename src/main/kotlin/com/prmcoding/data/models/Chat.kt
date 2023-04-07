package com.prmcoding.data.models

import com.prmcoding.responses.ChatDto
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Chat(
    val userIds: List<String>,
    val lastMessageId: String,
    val timestamp: Long,
    @BsonId
    val id: String = ObjectId().toString()
)

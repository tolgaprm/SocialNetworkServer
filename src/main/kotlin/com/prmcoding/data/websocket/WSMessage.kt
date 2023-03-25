package com.prmcoding.data.websocket

import com.prmcoding.data.models.Message
import kotlinx.serialization.Serializable

@Serializable
data class WSMessage(
    val fromId: String,
    val toId: String,
    val text: String,
    val timestamp: Long,
    val chatId: String?,
){
    fun toMessage():Message{
        return Message(
            fromId = fromId,
            toId = toId,
            text = text,
            timestamp = timestamp,
            chatId = chatId
        )
    }
}

package com.prmcoding.data.websocket

import com.prmcoding.data.models.Message
import kotlinx.serialization.Serializable

@Serializable
data class WSClientMessage(
    val toId: String,
    val text: String,
    val chatId: String?,
){
    fun toMessage(fromId:String):Message{
        return Message(
            fromId = fromId,
            toId = toId,
            text = text,
            timestamp = System.currentTimeMillis(),
            chatId = chatId
        )
    }
}

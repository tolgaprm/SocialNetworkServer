package com.prmcoding.responses

import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val chatId:String,
    val remoteUserId:String?,
    val remoteUsername: String?,
    val remoteUserProfileUrl: String?,
    val lastMessage: String?,
    val timestamp: Long?
)
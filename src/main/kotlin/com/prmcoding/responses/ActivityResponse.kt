package com.prmcoding.responses

import kotlinx.serialization.Serializable

@Serializable
data class ActivityResponse(
    val id: String,
    val timestamp: Long,
    val userId: String,
    val parentId: String,
    val type: Int,
    val username: String
)

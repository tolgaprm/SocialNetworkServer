package com.prmcoding.responses

import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    val id: String,
    val username: String,
    val profileImageUrl: String?,
    val timestamp: Long,
    val commentText: String,
    val likeCount: Int,
    val isLiked: Boolean,
)

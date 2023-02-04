package com.prmcoding.responses

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val id: String,
    val userId: String,
    val username: String,
    val imageUrl: String,
    val profilePictureProfile: String,
    val description: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean
)

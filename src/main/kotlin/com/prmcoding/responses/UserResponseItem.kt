package com.prmcoding.responses

@kotlinx.serialization.Serializable
data class UserResponseItem(
    val userId: String,
    val userName: String,
    val profilePictureUrl: String,
    val bio: String,
    val isFollowing: Boolean
)

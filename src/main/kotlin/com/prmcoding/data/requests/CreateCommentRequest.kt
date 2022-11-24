package com.prmcoding.data.requests

@kotlinx.serialization.Serializable
data class CreateCommentRequest(
    val comment: String,
    val postId: String,
    val userId: String
)

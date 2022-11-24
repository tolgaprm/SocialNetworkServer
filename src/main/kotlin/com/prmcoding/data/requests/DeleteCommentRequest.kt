package com.prmcoding.data.requests

@kotlinx.serialization.Serializable
data class DeleteCommentRequest(
    val userId: String,
    val commentId: String
)

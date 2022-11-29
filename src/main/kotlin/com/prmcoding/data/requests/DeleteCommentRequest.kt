package com.prmcoding.data.requests

@kotlinx.serialization.Serializable
data class DeleteCommentRequest(
    val commentId: String
)

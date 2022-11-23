package com.prmcoding.data.requests

@kotlinx.serialization.Serializable
data class CreatePostRequest(
    val userId: String,
    val description: String,
)

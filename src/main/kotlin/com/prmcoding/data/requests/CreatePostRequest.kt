package com.prmcoding.data.requests

@kotlinx.serialization.Serializable
data class CreatePostRequest(
    val description: String,
)

package com.prmcoding.data.requests

@kotlinx.serialization.Serializable
data class LikeUpdateRequest(
    val userId: String,
    val parentId: String
)

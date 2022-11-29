package com.prmcoding.data.requests

@kotlinx.serialization.Serializable
data class LikeUpdateRequest(
    val parentId: String,
    val parentType: Int
)

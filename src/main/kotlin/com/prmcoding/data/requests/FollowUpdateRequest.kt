package com.prmcoding.data.requests

@kotlinx.serialization.Serializable
data class FollowUpdateRequest(
    val followedUserId: String
)
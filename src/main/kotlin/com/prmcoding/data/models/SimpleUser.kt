package com.prmcoding.data.models

import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable
data class SimpleUser(
    val username: String,
    val profileImageUrl: String,
    val userId: String
)

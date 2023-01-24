package com.prmcoding.responses

@kotlinx.serialization.Serializable
data class AuthResponse(
    val userId: String,
    val token: String
)

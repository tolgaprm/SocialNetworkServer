package com.prmcoding.data.requests

@kotlinx.serialization.Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

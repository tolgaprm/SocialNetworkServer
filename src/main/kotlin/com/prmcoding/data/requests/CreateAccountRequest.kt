package com.prmcoding.data.requests

@kotlinx.serialization.Serializable
data class CreateAccountRequest(
    val username: String,
    val email: String,
    val password: String
)

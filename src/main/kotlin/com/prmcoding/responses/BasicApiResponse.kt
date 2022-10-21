package com.prmcoding.responses

@kotlinx.serialization.Serializable
data class BasicApiResponse(
    val successful: Boolean,
    val message: String? = null
)

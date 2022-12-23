package com.prmcoding.data.requests

import com.prmcoding.data.models.Skill
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val username: String,
    val bio: String,
    val gitHubUrl: String,
    val instagramUrl: String,
    val linkedInUrl: String,
    val skills: List<Skill>,
    val profileImageChanged: Boolean = false
)

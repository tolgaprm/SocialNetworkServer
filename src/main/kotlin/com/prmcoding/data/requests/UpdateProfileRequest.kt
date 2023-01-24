package com.prmcoding.data.requests

import com.prmcoding.responses.SkillsDto
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val username: String,
    val bio: String,
    val gitHubUrl: String,
    val instagramUrl: String,
    val linkedInUrl: String,
    val skills: List<SkillsDto>,
    val profileImageChanged: Boolean = false
)

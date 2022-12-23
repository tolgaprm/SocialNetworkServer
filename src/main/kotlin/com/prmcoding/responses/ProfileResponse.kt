package com.prmcoding.responses

import com.prmcoding.data.models.Skill
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val userId: String,
    val username: String,
    val bio: String,
    val followerCount: Int,
    val followingCount: Int,
    val postCount: Int,
    val bannerUrl: String,
    val profilePictureUrl: String,
    val topSkills: List<Skill>,
    val gitHubUrl: String?,
    val instagramUrl: String?,
    val linkedInUrl: String?,
    val isOwnProfile: Boolean,
    val isFollowing: Boolean
)

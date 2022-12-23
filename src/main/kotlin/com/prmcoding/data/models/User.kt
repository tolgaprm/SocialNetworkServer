package com.prmcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@kotlinx.serialization.Serializable
data class User(
    val email: String,
    val username: String,
    val password: String,
    val bannerUrl: String,
    val profileImageUrl: String,
    val bio: String,
    val skills: List<Skill> = listOf(),
    val gitHubUrl: String?,
    val instagramUrl: String?,
    val linkedInUrl: String?,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val postCount: Int = 0,
    @BsonId
    val id: String = ObjectId().toString()
)

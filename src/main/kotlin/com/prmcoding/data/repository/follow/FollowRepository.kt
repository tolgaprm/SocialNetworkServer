package com.prmcoding.data.repository.follow

import com.prmcoding.data.models.Following

interface FollowRepository {

    suspend fun followUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean

    suspend fun unFollowUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean


    suspend fun getFollowsByUser(userId: String): List<Following>

    suspend fun doesUserFollow(
        followingUserId: String,
        followedUserId: String
    ): Boolean
}
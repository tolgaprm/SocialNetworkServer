package com.prmcoding.service

import com.prmcoding.data.repository.follow.FollowRepository

class FollowService(
    private val repository: FollowRepository
) {

    suspend fun followUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean {
        return repository.followUserIfExists(
            followingUserId = followingUserId,
            followedUserId = followedUserId
        )
    }

    suspend fun unFollowUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean {
        return repository.unFollowUserIfExists(
            followingUserId = followingUserId,
            followedUserId = followedUserId
        )
    }
}
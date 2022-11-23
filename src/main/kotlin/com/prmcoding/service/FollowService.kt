package com.prmcoding.service

import com.prmcoding.data.repository.follow.FollowRepository
import com.prmcoding.data.requests.FollowUpdateRequest

class FollowService(
    private val repository: FollowRepository
) {

    suspend fun followUserIfExists(request: FollowUpdateRequest): Boolean {
        return repository.followUserIfExists(
            followingUserId = request.followingUserId,
            followedUserId = request.followedUserId
        )
    }

    suspend fun unFollowUserIfExists(request: FollowUpdateRequest): Boolean {
        return repository.unFollowUserIfExists(
            followingUserId = request.followingUserId,
            followedUserId = request.followedUserId
        )
    }
}
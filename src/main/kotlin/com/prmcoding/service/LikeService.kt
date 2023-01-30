package com.prmcoding.service

import com.prmcoding.data.repository.follow.FollowRepository
import com.prmcoding.data.repository.likes.LikeRepository
import com.prmcoding.data.repository.user.UserRepository
import com.prmcoding.responses.UserResponseItem

class LikeService(
    private val likeRepository: LikeRepository,
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {

    suspend fun likeParent(userId: String, parentId: String, parentType: Int): Boolean {
        return likeRepository.likeParent(userId = userId, parentId = parentId, parentType = parentType)
    }

    suspend fun unLikeParent(userId: String, parentId: String, parentType: Int): Boolean {
        return likeRepository.unLikeParent(userId = userId, parentId = parentId, parentType = parentType)
    }

    suspend fun deleteLikesForParent(parentId: String) {
        likeRepository.deleteLikesForParent(parentId = parentId)
    }

    suspend fun getUsersWhoLikedParent(
        parentId: String,
        userId: String,
        page: Int = 0,
        pageSize: Int = 0
    ): List<UserResponseItem> {
        val userIds = likeRepository.getLikesForParent(
            parentId = parentId,
            page = page,
            pageSize = pageSize
        ).map { it.userId }
        val users = userRepository.getUsersByIds(userIds = userIds)
        val followsByUser = followRepository.getFollowsByUser(userId = userId)
        return users.map { user ->
            val isFollowing = followsByUser.find { it.followedUserId == user.id } != null
            UserResponseItem(
                userId = user.id,
                userName = user.username,
                profilePictureUrl = user.profileImageUrl,
                bio = user.bio,
                isFollowing = isFollowing
            )
        }
    }
}
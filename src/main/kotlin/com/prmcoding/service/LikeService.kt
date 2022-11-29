package com.prmcoding.service

import com.prmcoding.data.repository.likes.LikeRepository

class LikeService(
    private val repository: LikeRepository
) {

    suspend fun likeParent(userId: String, parentId: String, parentType: Int): Boolean {
        return repository.likeParent(userId = userId, parentId = parentId, parentType = parentType)
    }

    suspend fun unLikeParent(userId: String, parentId: String): Boolean {
        return repository.unLikeParent(userId = userId, parentId = parentId)
    }

    suspend fun deleteLikesForParent(parentId: String) {
        repository.deleteLikesForParent(parentId = parentId)
    }

}
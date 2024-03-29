package com.prmcoding.data.repository.likes

import com.prmcoding.data.models.Like

interface LikeRepository {

    suspend fun likeParent(userId: String, parentId: String, parentType: Int): Boolean

    suspend fun unLikeParent(userId: String, parentId: String, parentType: Int): Boolean

    suspend fun deleteLikesForParent(parentId: String)

    suspend fun getLikesForParent(
        parentId: String
    ): List<Like>
}
package com.prmcoding.data.repository.likes

interface LikeRepository {

    suspend fun likeParent(userId: String, parentId: String): Boolean

    suspend fun unLikeParent(userId: String, parentId: String): Boolean

    suspend fun deleteLikesForParent(parentId: String)
}
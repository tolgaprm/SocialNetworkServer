package com.prmcoding.data.repository.comment

import com.prmcoding.data.models.Comment

interface CommentRepository {

    suspend fun createComment(comment: Comment): String

    suspend fun deleteComment(commentId: String): Boolean

    suspend fun getCommentForPost(postId: String): List<Comment>

    suspend fun getComment(commentId: String): Comment?

    suspend fun deleteCommentForParent(parentId: String)
}
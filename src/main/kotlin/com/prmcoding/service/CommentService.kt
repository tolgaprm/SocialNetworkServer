package com.prmcoding.service

import com.prmcoding.data.models.Comment
import com.prmcoding.data.repository.comment.CommentRepository
import com.prmcoding.data.requests.CreateCommentRequest
import com.prmcoding.util.Constants

class CommentService(
    private val repository: CommentRepository
) {

    suspend fun createComment(
        createCommentRequest: CreateCommentRequest,
        userId: String
    ): ValidationEvent {

        createCommentRequest.apply {
            if (comment.isBlank() || postId.isBlank()) {
                return ValidationEvent.ErrorFieldEmpty
            }
            if (comment.length > Constants.MAX_COMMENT_LENGTH) {
                return ValidationEvent.ErrorCommentTooLong
            }
        }

        repository.createComment(
            Comment(
                comment = createCommentRequest.comment,
                userId = userId,
                postId = createCommentRequest.postId,
                timestamp = System.currentTimeMillis()
            )
        )
        return ValidationEvent.Success
    }

    suspend fun deleteComment(commentId: String): Boolean {
        return repository.deleteComment(commentId = commentId)
    }

    suspend fun getCommentsForPost(postId: String): List<Comment> {
        return repository.getCommentForPost(postId = postId)
    }

    suspend fun deleteCommentsForParent(parentId: String) {
        return repository.deleteCommentForParent(parentId = parentId)
    }

    suspend fun getCommentById(commentId: String): Comment? {
        return repository.getComment(commentId = commentId)
    }


    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object ErrorCommentTooLong : ValidationEvent()
        object Success : ValidationEvent()
    }
}
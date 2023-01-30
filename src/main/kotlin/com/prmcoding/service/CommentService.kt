package com.prmcoding.service

import com.prmcoding.data.models.Comment
import com.prmcoding.data.repository.comment.CommentRepository
import com.prmcoding.data.repository.user.UserRepository
import com.prmcoding.data.requests.CreateCommentRequest
import com.prmcoding.responses.CommentResponse
import com.prmcoding.util.Constants

class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
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

        val user = userRepository.getUserById(id = userId) ?: return ValidationEvent.UserNotFound
        Comment(
            comment = createCommentRequest.comment,
            userId = userId,
            postId = createCommentRequest.postId,
            timestamp = System.currentTimeMillis(),
            username = user.username,
            profileImageUrl = user.profileImageUrl,
            likeCount = 0
        )
        return ValidationEvent.Success
    }

    suspend fun deleteComment(commentId: String): Boolean {
        return commentRepository.deleteComment(commentId = commentId)
    }

    suspend fun getCommentsForPost(postId: String): List<CommentResponse> {
        return commentRepository.getCommentForPost(postId = postId)
    }

    suspend fun deleteCommentsForParent(parentId: String) {
        return commentRepository.deleteCommentForParent(parentId = parentId)
    }

    suspend fun getCommentById(commentId: String): Comment? {
        return commentRepository.getComment(commentId = commentId)
    }


    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object ErrorCommentTooLong : ValidationEvent()
        object Success : ValidationEvent()
        object UserNotFound : ValidationEvent()
    }
}
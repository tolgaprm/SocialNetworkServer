package com.prmcoding.data.repository.comment

import com.prmcoding.data.models.Comment
import com.prmcoding.data.models.Like
import com.prmcoding.responses.CommentResponse
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class CommentRepositoryImpl(
    db: CoroutineDatabase
) : CommentRepository {

    private val comments = db.getCollection<Comment>()
    private val likes = db.getCollection<Like>()
    override suspend fun createComment(comment: Comment): String {
        comments.insertOne(comment)
        return comment.id
    }

    override suspend fun deleteComment(commentId: String): Boolean {
        val deleteCount = comments.deleteOneById(commentId).deletedCount
        return deleteCount > 0
    }

    override suspend fun getCommentForPost(postId: String): List<CommentResponse> {
        return comments.find(Comment::postId eq postId).toList().map { comment ->
            val isLiked = likes.findOne(
                Like::parentId eq comment.id
            ) != null
            CommentResponse(
                id = comment.id,
                username = comment.username,
                profileImageUrl = comment.profileImageUrl,
                timestamp = comment.timestamp,
                commentText = comment.comment,
                isLiked = isLiked,
                likeCount = comment.likeCount
            )
        }
    }

    override suspend fun getComment(commentId: String): Comment? {
        return comments.findOneById(commentId)
    }

    override suspend fun deleteCommentForParent(parentId: String) {
        comments.deleteMany(Comment::postId eq parentId)
    }
}
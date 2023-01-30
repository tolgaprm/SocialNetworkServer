package com.prmcoding.data.repository.likes

import com.prmcoding.data.models.Comment
import com.prmcoding.data.models.Like
import com.prmcoding.data.models.Post
import com.prmcoding.data.models.User
import com.prmcoding.data.util.ParentType
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class LikesRepositoryImpl(
    db: CoroutineDatabase
) : LikeRepository {

    private val likes = db.getCollection<Like>()
    private val users = db.getCollection<User>()
    private val comments = db.getCollection<Comment>()
    private val posts = db.getCollection<Post>()

    override suspend fun likeParent(userId: String, parentId: String, parentType: Int): Boolean {
        val doesUserExist = users.findOneById(userId) != null

        return if (doesUserExist) {
            when (parentType) {
                ParentType.Post.type -> {
                    val post = posts.findOneById(parentId) ?: return false
                    posts.updateOneById(
                        id = parentId,
                        update = setValue(Post::likeCount, post.likeCount + 1)
                    )
                }

                ParentType.Comment.type -> {
                    val comment = comments.findOneById(parentId) ?: return false
                    comments.updateOneById(
                        id = parentId,
                        update = setValue(Comment::likeCount, comment.likeCount + 1)
                    )
                }
            }
            likes.insertOne(
                Like(
                    userId = userId,
                    parentId = parentId,
                    parentType = parentType,
                    timestamp = System.currentTimeMillis()
                )
            )
            true
        } else {
            false
        }
    }

    override suspend fun unLikeParent(
        userId: String,
        parentId: String,
        parentType: Int
    ): Boolean {
        val doesUserExist = users.findOneById(userId) != null
        return if (doesUserExist) {
            when (parentType) {
                ParentType.Post.type -> {
                    val post = posts.findOneById(parentId) ?: return false
                    posts.updateOneById(
                        id = parentId,
                        update = setValue(Post::likeCount, (post.likeCount - 1).coerceAtLeast(0))
                    )
                }

                ParentType.Comment.type -> {
                    val comment = comments.findOneById(parentId) ?: return false
                    comments.updateOneById(
                        id = parentId,
                        update = setValue(Comment::likeCount, (comment.likeCount - 1).coerceAtLeast(0))
                    )
                }
            }
            likes.deleteOne(
                and(
                    Like::userId eq userId,
                    Like::parentId eq parentId
                )
            )
            true
        } else {
            false
        }
    }

    override suspend fun deleteLikesForParent(parentId: String) {
        likes.deleteMany(Like::parentId eq parentId)
    }

    override suspend fun getLikesForParent(
        parentId: String,
        page: Int,
        pageSize: Int
    ): List<Like> {
        return likes.find(Like::parentId eq parentId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Like::timestamp)
            .toList()
    }
}
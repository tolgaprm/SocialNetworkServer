package com.prmcoding.service

import com.prmcoding.data.models.Activity
import com.prmcoding.data.repository.activity.ActivityRepository
import com.prmcoding.data.repository.comment.CommentRepository
import com.prmcoding.data.repository.post.PostRepository
import com.prmcoding.data.util.ActivityType
import com.prmcoding.data.util.ParentType
import com.prmcoding.responses.ActivityResponse
import com.prmcoding.util.Constants

class ActivityService(
    private val activityRepository: ActivityRepository,
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) {
    suspend fun getActivitiesForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_ACTIVITY_PAGE_SIZE
    ): List<ActivityResponse> {
        return activityRepository.getActivitiesForUser(
            userId = userId,
            page = page,
            pageSize = pageSize
        )
    }


    suspend fun createActivity(activity: Activity) {
        activityRepository.createActivity(activity)
    }

    suspend fun addCommentActivity(
        byUserId: String,
        postId: String
    ): Boolean {
        val userIdOfPost = postRepository.getPost(postId = postId)?.userId ?: return false

        if (byUserId == userIdOfPost) {
            return false
        }

        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                byUserId = byUserId,
                toUserId = userIdOfPost,
                type = ActivityType.CommentedOnPost.type,
                parentId = postId,
            )
        )
        return true
    }

    suspend fun addLikeActivity(
        byUserId: String,
        parentType: ParentType,
        parentId: String
    ): Boolean {

        val toUserId = when (parentType) {
            is ParentType.Post -> {
                postRepository.getPost(parentId)?.userId
            }

            is ParentType.Comment -> {
                commentRepository.getComment(parentId)?.userId
            }

            is ParentType.None -> {
                return false
            }
        } ?: return false

        if (byUserId == toUserId) {
            return false
        }

        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                byUserId = byUserId,
                toUserId = toUserId,
                type = when (parentType) {
                    is ParentType.Post -> ActivityType.LikedPost.type
                    is ParentType.Comment -> ActivityType.LikedComment.type
                    else -> ActivityType.LikedPost.type
                },
                parentId = parentId,
            )
        )
        return true
    }

    suspend fun deleteActivity(activityId: String): Boolean {
        return activityRepository.deleteActivity(activityId)
    }
}
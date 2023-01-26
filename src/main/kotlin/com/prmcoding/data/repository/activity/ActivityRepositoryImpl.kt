package com.prmcoding.data.repository.activity

import com.prmcoding.data.models.Activity
import com.prmcoding.data.models.User
import com.prmcoding.responses.ActivityResponse
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.`in`

class ActivityRepositoryImpl(
    db: CoroutineDatabase
) : ActivityRepository {

    private val activities = db.getCollection<Activity>()
    private val users = db.getCollection<User>()

    override suspend fun getActivitiesForUser(userId: String, page: Int, pageSize: Int): List<ActivityResponse> {

        val activities = activities.find(Activity::toUserId eq userId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Activity::timestamp)
            .toList()

        val userIds = activities.map { it.byUserId }
        val users = users.find(User::id `in` userIds).toList()
        return activities.mapIndexed { index, activity ->
            ActivityResponse(
                id = activity.id,
                userId = activity.byUserId,
                parentId = activity.toUserId,
                timestamp = activity.timestamp,
                type = activity.type,
                username = users[index].username
            )
        }
    }

    override suspend fun createActivity(activity: Activity) {
        activities.insertOne(activity)
    }

    override suspend fun deleteActivity(activityId: String): Boolean {
        return activities.deleteOneById(activityId).wasAcknowledged()
    }
}
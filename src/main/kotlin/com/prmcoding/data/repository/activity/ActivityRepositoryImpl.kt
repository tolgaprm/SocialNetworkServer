package com.prmcoding.data.repository.activity

import com.prmcoding.data.models.Activity
import org.litote.kmongo.`in`
import org.litote.kmongo.coroutine.CoroutineDatabase

class ActivityRepositoryImpl(
    db: CoroutineDatabase
) : ActivityRepository {

    private val activities = db.getCollection<Activity>()

    override suspend fun getActivitiesForUser(userId: String, page: Int, pageSize: Int): List<Activity> {

        return activities.find(Activity::toUserId `in` userId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Activity::timestamp)
            .toList()
    }

    override suspend fun createActivity(activity: Activity) {
        activities.insertOne(activity)
    }

    override suspend fun deleteActivity(activityId: String): Boolean {
        return activities.deleteOneById(activityId).wasAcknowledged()
    }
}
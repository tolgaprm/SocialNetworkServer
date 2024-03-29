package com.prmcoding.data.repository.activity

import com.prmcoding.data.models.Activity
import com.prmcoding.responses.ActivityResponse
import com.prmcoding.util.Constants

interface ActivityRepository {


    suspend fun getActivitiesForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_ACTIVITY_PAGE_SIZE
    ): List<ActivityResponse>

    suspend fun createActivity(activity: Activity)

    suspend fun deleteActivity(activityId: String): Boolean


}
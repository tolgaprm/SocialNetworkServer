package com.prmcoding.routes.activity

import com.prmcoding.routes.userId
import com.prmcoding.service.ActivityService
import com.prmcoding.util.Constants
import com.prmcoding.util.QueryParameters
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getActivities(
    activityService: ActivityService
) {
    authenticate {
        get("/api/activity/get") {

            val page = call.parameters[QueryParameters.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize =
                call.parameters[QueryParameters.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_SIZE

            val activities = activityService.getActivitiesForUser(
                userId = call.userId,
                page = page,
                pageSize = pageSize
            )

            call.respond(
                status = HttpStatusCode.OK,
                message = activities
            )
        }
    }
}
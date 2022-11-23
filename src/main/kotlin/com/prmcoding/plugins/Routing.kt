package com.prmcoding.plugins

import com.prmcoding.routes.follow.followUserRoute
import com.prmcoding.routes.follow.unFollowUserRoute
import com.prmcoding.routes.post.createPostRoute
import com.prmcoding.routes.user.createUserRoute
import com.prmcoding.routes.user.loginUserRoute
import com.prmcoding.service.FollowService
import com.prmcoding.service.PostService
import com.prmcoding.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()


    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {
        // User Routes
        createUserRoute(userService = userService)
        loginUserRoute(
            userService = userService,
            jwtAudience = jwtAudience,
            jwtIssuer = jwtIssuer,
            jwtSecret = jwtSecret,
        )

        // Following Routes
        followUserRoute(followService = followService)
        unFollowUserRoute(followService = followService)

        // Post Routes
        createPostRoute(
            postService = postService,
            userService = userService
        )

    }
}

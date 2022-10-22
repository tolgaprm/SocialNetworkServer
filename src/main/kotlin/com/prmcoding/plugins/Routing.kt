package com.prmcoding.plugins

import com.prmcoding.data.repository.follow.FollowRepository
import com.prmcoding.data.repository.post.PostRepository
import com.prmcoding.data.repository.user.UserRepository
import com.prmcoding.routes.follow.followUserRoute
import com.prmcoding.routes.follow.unFollowUserRoute
import com.prmcoding.routes.post.createPostRoute
import com.prmcoding.routes.user.createUserRoute
import com.prmcoding.routes.user.loginUserRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    val followRepository: FollowRepository by inject()
    val postRepository: PostRepository by inject()

    routing {
        // User Routes
        createUserRoute(userRepository = userRepository)
        loginUserRoute(userRepository = userRepository)

        // Following Routes
        followUserRoute(followRepository = followRepository)
        unFollowUserRoute(followRepository = followRepository)

        // Post Routes
        createPostRoute(postRepository = postRepository)

    }
}

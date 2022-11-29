package com.prmcoding.plugins

import com.prmcoding.routes.activity.getActivities
import com.prmcoding.routes.comment.createCommentRoute
import com.prmcoding.routes.comment.deleteCommentRoute
import com.prmcoding.routes.comment.getCommentsForPostRoute
import com.prmcoding.routes.follow.followUserRoute
import com.prmcoding.routes.follow.unFollowUserRoute
import com.prmcoding.routes.likes.likeParentRoute
import com.prmcoding.routes.likes.unlikeParentRoute
import com.prmcoding.routes.post.createPostRoute
import com.prmcoding.routes.post.deletePostRoute
import com.prmcoding.routes.post.getPostForFollowsRoute
import com.prmcoding.routes.user.createUserRoute
import com.prmcoding.routes.user.loginUserRoute
import com.prmcoding.service.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()
    val likeService: LikeService by inject()
    val commentService: CommentService by inject()
    val activityService: ActivityService by inject()

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
        followUserRoute(followService = followService, activityService = activityService)
        unFollowUserRoute(followService = followService)

        // Post Routes
        createPostRoute(postService = postService)
        getPostForFollowsRoute(postService = postService)
        deletePostRoute(
            postService = postService,
            likeService = likeService,
            commentService = commentService
        )

        // Like Routes
        likeParentRoute(likeService = likeService, activityService = activityService)
        unlikeParentRoute(likeService = likeService)

        // Comment Routes
        createCommentRoute(commentService = commentService, activityService = activityService)
        deleteCommentRoute(commentService = commentService, likeService = likeService)
        getCommentsForPostRoute(commentService = commentService)

        // Activity Routes
        getActivities(activityService = activityService)
    }
}

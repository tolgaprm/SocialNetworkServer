package com.prmcoding.plugins

import com.prmcoding.routes.activity.getActivities
import com.prmcoding.routes.comment.createComment
import com.prmcoding.routes.comment.deleteComment
import com.prmcoding.routes.comment.getCommentsForPost
import com.prmcoding.routes.follow.followUser
import com.prmcoding.routes.follow.unFollowUser
import com.prmcoding.routes.likes.likeParent
import com.prmcoding.routes.likes.unlikeParent
import com.prmcoding.routes.post.createPost
import com.prmcoding.routes.post.deletePost
import com.prmcoding.routes.post.getPostForFollows
import com.prmcoding.routes.user.*
import com.prmcoding.service.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
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
        createUser(userService = userService)
        loginUser(
            userService = userService,
            jwtAudience = jwtAudience,
            jwtIssuer = jwtIssuer,
            jwtSecret = jwtSecret,
        )
        searchUser(userService = userService)
        getUserProfile(userService = userService)
        getPostsForProfile(postService = postService)
        updateUserProfile(userService = userService)

        // Following Routes
        followUser(followService = followService, activityService = activityService)
        unFollowUser(followService = followService)

        // Post Routes
        createPost(postService = postService)
        getPostForFollows(postService = postService)
        deletePost(
            postService = postService,
            likeService = likeService,
            commentService = commentService
        )

        // Like Routes
        likeParent(likeService = likeService, activityService = activityService)
        unlikeParent(likeService = likeService)

        // Comment Routes
        createComment(commentService = commentService, activityService = activityService)
        deleteComment(commentService = commentService, likeService = likeService)
        getCommentsForPost(commentService = commentService)

        // Activity Routes
        getActivities(activityService = activityService)

        static {
            resources("static")
        }
    }
}

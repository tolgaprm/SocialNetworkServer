package com.prmcoding.data.repository.post

import com.prmcoding.data.models.Post
import com.prmcoding.responses.PostResponse
import com.prmcoding.util.Constants.DEFAULT_PAGE_SIZE

interface PostRepository {

    suspend fun createPost(post: Post): Boolean

    suspend fun deletePost(postId: String)

    suspend fun getPostsByFollows(
        ownUserId: String,
        page: Int = 0,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): List<PostResponse>

    suspend fun getPostsForProfile(
        ownUserId: String,
        userId: String,
        page: Int = 0,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): List<PostResponse>

    suspend fun getPost(postId: String): Post?

    suspend fun getPostDetails(
        userId: String,
        postId: String
    ): PostResponse?


}
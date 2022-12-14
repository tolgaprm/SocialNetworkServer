package com.prmcoding.data.repository.post

import com.prmcoding.data.models.Post
import com.prmcoding.util.Constants.DEFAULT_POST_PAGE_SIZE

interface PostRepository {

    suspend fun createPost(post: Post): Boolean

    suspend fun deletePost(postId: String)

    suspend fun getPostsByFollows(
        userId: String,
        page: Int = 0,
        pageSize: Int = DEFAULT_POST_PAGE_SIZE
    ): List<Post>

    suspend fun getPostsForProfile(
        userId: String,
        page: Int = 0,
        pageSize: Int = DEFAULT_POST_PAGE_SIZE
    ): List<Post>

    suspend fun getPost(postId: String): Post?


}
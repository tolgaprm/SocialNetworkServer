package com.prmcoding.service

import com.prmcoding.data.models.Post
import com.prmcoding.data.repository.post.PostRepository
import com.prmcoding.data.requests.CreatePostRequest
import com.prmcoding.util.Constants

class PostService(
    private val postRepository: PostRepository
) {

    suspend fun createPostIfUserExists(request: CreatePostRequest): Boolean {
        return postRepository.createPostIfUserExists(
            Post(
                imageUrl = "",
                userId = request.userId,
                timestamp = System.currentTimeMillis(),
                description = request.description
            )
        )
    }

    suspend fun getPostsForFollows(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<Post> {
        return postRepository.getPostsByFollows(
            userId = userId,
            page = page,
            pageSize = pageSize
        )
    }

    suspend fun getPost(postId: String): Post? = postRepository.getPost(postId)

    suspend fun deletePost(postId: String) {
        postRepository.deletePost(postId = postId)
    }
}
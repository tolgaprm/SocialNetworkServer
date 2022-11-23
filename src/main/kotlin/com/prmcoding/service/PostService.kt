package com.prmcoding.service

import com.prmcoding.data.models.Post
import com.prmcoding.data.repository.post.PostRepository
import com.prmcoding.data.requests.CreatePostRequest

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
}
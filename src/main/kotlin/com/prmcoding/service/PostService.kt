package com.prmcoding.service

import com.prmcoding.data.models.Post
import com.prmcoding.data.repository.post.PostRepository
import com.prmcoding.data.requests.CreatePostRequest
import com.prmcoding.responses.PostResponse
import com.prmcoding.util.Constants

class PostService(
    private val postRepository: PostRepository
) {

    suspend fun createPost(request: CreatePostRequest, userId: String, imageUrl: String): Boolean {
        return postRepository.createPost(
            Post(
                imageUrl = imageUrl,
                userId = userId,
                timestamp = System.currentTimeMillis(),
                description = request.description
            )
        )
    }

    suspend fun getPostsForProfile(
        ownUserId: String,
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<PostResponse> {
        return postRepository.getPostsForProfile(
            ownUserId = ownUserId,
            userId = userId,
            page = page,
            pageSize = pageSize
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

    suspend fun getPost(postId: String): Post? {
        return postRepository.getPost(postId = postId)
    }

    suspend fun getPostDetails(postId: String, userId: String): PostResponse? {
        return postRepository.getPostDetails(postId = postId, userId = userId)
    }

    suspend fun deletePost(postId: String) {
        postRepository.deletePost(postId = postId)
    }
}
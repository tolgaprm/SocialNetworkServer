package com.prmcoding.service

import com.prmcoding.data.models.User
import com.prmcoding.data.repository.follow.FollowRepository
import com.prmcoding.data.repository.user.UserRepository
import com.prmcoding.data.requests.CreateAccountRequest
import com.prmcoding.data.requests.LoginRequest
import com.prmcoding.data.requests.UpdateProfileRequest
import com.prmcoding.responses.ProfileResponse
import com.prmcoding.responses.UserResponseItem


class UserService(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {

    suspend fun doesUserWithEmailExist(email: String): Boolean {
        return userRepository.getUserByEmail(email) != null
    }

    suspend fun createUser(request: CreateAccountRequest) {
        userRepository.createUser(
            User(
                email = request.email,
                username = request.username,
                password = request.password,
                profileImageUrl = "",
                bannerUrl = "",
                bio = "",
                gitHubUrl = "",
                instagramUrl = "",
                linkedInUrl = ""
            )
        )
    }

    suspend fun updateUser(
        userId: String,
        profileImageUrl: String?,
        bannerUrl: String?,
        updateProfileRequest: UpdateProfileRequest
    ): Boolean {
        return userRepository.updateUser(
            userId = userId,
            profileImageUrl = profileImageUrl,
            bannerUrl = bannerUrl,
            updateProfileRequest = updateProfileRequest
        )
    }


    // CallerUserId which want to see userId Profile
    suspend fun getUserProfile(userId: String, callerUserId: String): ProfileResponse? {
        val user = userRepository.getUserById(userId) ?: return null
        return ProfileResponse(
            userId = user.id,
            username = user.username,
            bio = user.bio,
            followerCount = user.followerCount,
            followingCount = user.followingCount,
            postCount = user.postCount,
            bannerUrl = user.bannerUrl,
            profilePictureUrl = user.profileImageUrl,
            topSkills = user.skills.map { it.toSkill() },
            gitHubUrl = user.gitHubUrl,
            instagramUrl = user.instagramUrl,
            linkedInUrl = user.linkedInUrl,
            isOwnProfile = userId == callerUserId,
            isFollowing = if (userId != callerUserId) {
                followRepository.doesUserFollow(
                    followingUserId = callerUserId,
                    followedUserId = userId
                )
            } else {
                false
            }
        )

    }


    suspend fun getUserByEmail(email: String): User? {
        return userRepository.getUserByEmail(email)
    }

    fun isValidPassword(enteredPassword: String, actualPassword: String): Boolean {
        return enteredPassword == actualPassword
    }

    suspend fun searchForUsers(query: String, userId: String): List<UserResponseItem> {
        val users = userRepository.searchForUsers(query = query)
        val followsByUser = followRepository.getFollowsByUser(userId = userId)

        return users.map { user ->
            val isFollowing = followsByUser.find {
                it.followedUserId == user.id
            } != null

            UserResponseItem(
                userId = user.id,
                userName = user.username,
                profilePictureUrl = user.profileImageUrl,
                bio = user.bio,
                isFollowing = isFollowing
            )
        }.filter { it.userId != userId }
    }

    fun validateLoginRequest(request: LoginRequest): ValidationEvent {
        if (request.email.isBlank() || request.password.isBlank()) {
            return ValidationEvent.ErrorFieldEmpty
        }
        return ValidationEvent.Success
    }

    fun validateCreateAccountRequest(request: CreateAccountRequest): ValidationEvent {
        if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
            return ValidationEvent.ErrorFieldEmpty
        }
        return ValidationEvent.Success
    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object Success : ValidationEvent()
    }
}
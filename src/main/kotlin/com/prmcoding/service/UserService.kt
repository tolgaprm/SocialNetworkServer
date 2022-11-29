package com.prmcoding.service

import com.prmcoding.data.models.User
import com.prmcoding.data.repository.follow.FollowRepository
import com.prmcoding.data.repository.user.UserRepository
import com.prmcoding.data.requests.CreateAccountRequest
import com.prmcoding.data.requests.LoginRequest
import com.prmcoding.responses.UserResponseItem

// ViewModel'a benzer bir yapı, bu servis yapısında rpute içerisinde uygulanan logic işlemeri burada yapıyoruz.
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
                bio = "",
                gitHubUrl = "",
                instagramUrl = "",
                linkedInUrl = ""
            )
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
                userName = user.username,
                profilePictureUrl = user.profileImageUrl,
                bio = user.bio,
                isFollowing = isFollowing
            )
        }
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
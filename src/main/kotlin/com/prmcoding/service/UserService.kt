package com.prmcoding.service

import com.prmcoding.data.models.User
import com.prmcoding.data.repository.user.UserRepository
import com.prmcoding.data.requests.CreateAccountRequest
import com.prmcoding.data.requests.LoginRequest

// ViewModel'a benzer bir yapı, bu servis yapısında rpute içerisinde uygulanan logic işlemeri burada yapıyoruz.
class UserService(
    private val repository: UserRepository
) {

    suspend fun doesUserWithEmailExist(email: String): Boolean {
        return repository.getUserByEmail(email) != null
    }

    suspend fun createUser(request: CreateAccountRequest) {
        repository.createUser(
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

    suspend fun doesPasswordForUserMatch(request: LoginRequest): Boolean {
        return repository.doesPasswordForUserMatch(
            email = request.email,
            enteredPassword = request.password
        )
    }

    suspend fun getUserByEmail(email: String): User? {
        return repository.getUserByEmail(email)
    }

    fun isValidPassword(enteredPassword: String, actualPassword: String): Boolean {
        return enteredPassword == actualPassword
    }

    suspend fun doesEmailBelongsToUserId(email: String, userId: String): Boolean {
        return repository.doesEmailBelongToUserId(
            email = email,
            userId = userId
        )
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
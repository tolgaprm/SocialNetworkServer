package com.prmcoding.controller.user

import com.prmcoding.data.models.User

interface UserController {
    suspend fun createUser(user: User)

    suspend fun getUserById(id: String): User?

    suspend fun getUserByEmail(email: String): User?
}
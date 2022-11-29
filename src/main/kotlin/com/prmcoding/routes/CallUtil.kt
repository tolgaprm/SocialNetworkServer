package com.prmcoding.routes


import com.prmcoding.plugins.userId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*


/**
Here we get the userId which I saved userId in JWT(token) when user login
 */
val ApplicationCall.userId:String
    get() = principal<JWTPrincipal>()?.userId.toString()

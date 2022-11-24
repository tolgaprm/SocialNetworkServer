package com.prmcoding.util

import com.prmcoding.plugins.email
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

/**
 Here we get the email which I saved email in JWT(token) when user login
  */
suspend fun PipelineContext<Unit, ApplicationCall>.ifEmailBelongToUserId(
    userId: String,
    validateEmail: suspend (email: String, userId: String) -> Boolean,
    onSuccess: suspend () -> Unit
) {
    val isEmailByUser = validateEmail(
        call.principal<JWTPrincipal>()?.email ?: "",
        userId
    )

    if (isEmailByUser) {
        onSuccess()
    } else {
        call.respond(
            status = HttpStatusCode.Unauthorized,
            message = "You are not who you say you are"
        )
    }
}
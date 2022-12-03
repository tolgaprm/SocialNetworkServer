package com.prmcoding.routes.user

import com.google.common.truth.Truth.assertThat
import com.prmcoding.data.models.User
import com.prmcoding.data.requests.CreateAccountRequest
import com.prmcoding.di.testModule
import com.prmcoding.plugins.configureRouting
import com.prmcoding.repository.user.FakeUserRepository
import com.prmcoding.responses.BasicApiResponse
import com.prmcoding.service.UserService
import com.prmcoding.util.ApiResponseMessages
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.core.context.GlobalContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


class CreateUserRouteTest : KoinTest {

    private val userRepository by inject<FakeUserRepository>()
    private val userService by inject<UserService>()

    @BeforeTest
    fun setup() {

        if (GlobalContext.getOrNull() != null) {
            stopKoin()
            startKoin {
                modules(testModule)
            }
        } else if (GlobalContext.getOrNull() == null) {
            startKoin {
                modules(testModule)
            }
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `Create user, no body attached, responds with BadRequest`() {
        testApplication {

            application {
                configureRouting()
            }

            val response = client.post(urlString = "/api/user/create")

            assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)

        }
    }


    @Test
    fun `Create user, user already exists, responds with unsuccessful`() = testApplication {

        application {
            install(Routing) {
                createUser(userService = user)
            }
        }

        val user = User(
            email = "test@test.com",
            username = "test",
            password = "test",
            profileImageUrl = "",
            bio = "",
            gitHubUrl = "",
            instagramUrl = "",
            linkedInUrl = ""
        )
        userRepository.createUser(
            user = user
        )


        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post(urlString = "/api/user/create") {

            contentType(ContentType.Application.Json)
            val createAccountRequest = CreateAccountRequest(
                email = "test@test.com",
                username = "test",
                password = "test",
            )

            setBody(createAccountRequest)
        }

        val responseText = Json.decodeFromString<BasicApiResponse>(response.bodyAsText())

        assertThat(
            BasicApiResponse(
                successful = false,
                message = ApiResponseMessages.USER_ALREADY_EXISTS
            )
        ).isEqualTo(responseText)

    }

    @Test
    fun `Create user, email is empty, responds with unsuccessful`() = testApplication {

        application {
            configureRouting()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post(urlString = "/api/user/create") {

            contentType(ContentType.Application.Json)
            val createAccountRequest = CreateAccountRequest(
                email = "",
                username = "",
                password = "",
            )

            setBody(createAccountRequest)

        }

        val responseText = Json.decodeFromString<BasicApiResponse>(response.bodyAsText())

        assertThat(
            BasicApiResponse(
                successful = false,
                message = ApiResponseMessages.FIELDS_BLANK
            )
        ).isEqualTo(responseText)

    }
}


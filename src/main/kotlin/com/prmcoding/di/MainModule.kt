package com.prmcoding.di


import com.prmcoding.controller.user.UserController
import com.prmcoding.controller.user.UserControllerImpl
import com.prmcoding.util.Constants.DATABASE_NAME
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(DATABASE_NAME)
    }

    single<UserController> {
        UserControllerImpl(get())
    }
}
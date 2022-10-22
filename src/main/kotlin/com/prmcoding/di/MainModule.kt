package com.prmcoding.di


import com.prmcoding.data.repository.follow.FollowRepository
import com.prmcoding.data.repository.follow.FollowRepositoryImpl
import com.prmcoding.data.repository.user.UserRepository
import com.prmcoding.data.repository.user.UserRepositoryImpl
import com.prmcoding.util.Constants.DATABASE_NAME
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(DATABASE_NAME)
    }

    single<UserRepository> {
        UserRepositoryImpl(get())
    }

    single<FollowRepository> {
        FollowRepositoryImpl(get())
    }

}
package com.prmcoding.di


import com.prmcoding.data.repository.follow.FollowRepository
import com.prmcoding.data.repository.follow.FollowRepositoryImpl
import com.prmcoding.data.repository.post.PostRepository
import com.prmcoding.data.repository.post.PostRepositoryImpl
import com.prmcoding.data.repository.user.UserRepository
import com.prmcoding.data.repository.user.UserRepositoryImpl
import com.prmcoding.service.FollowService
import com.prmcoding.service.PostService
import com.prmcoding.service.UserService
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

    single<PostRepository> {
        PostRepositoryImpl(get())
    }

    single {
        UserService(get())
    }

    single {
        FollowService(get())
    }

    single {
        PostService(get())
    }

}
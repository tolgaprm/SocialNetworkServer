package com.prmcoding.di


import com.prmcoding.data.repository.activity.ActivityRepository
import com.prmcoding.data.repository.activity.ActivityRepositoryImpl
import com.prmcoding.data.repository.comment.CommentRepository
import com.prmcoding.data.repository.comment.CommentRepositoryImpl
import com.prmcoding.data.repository.follow.FollowRepository
import com.prmcoding.data.repository.follow.FollowRepositoryImpl
import com.prmcoding.data.repository.likes.LikeRepository
import com.prmcoding.data.repository.likes.LikesRepositoryImpl
import com.prmcoding.data.repository.post.PostRepository
import com.prmcoding.data.repository.post.PostRepositoryImpl
import com.prmcoding.data.repository.user.UserRepository
import com.prmcoding.data.repository.user.UserRepositoryImpl
import com.prmcoding.service.*
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

    single<LikeRepository> {
        LikesRepositoryImpl(get())
    }

    single<CommentRepository> {
        CommentRepositoryImpl(get())
    }

    single<ActivityRepository> {
        ActivityRepositoryImpl(get())
    }

    single { UserService(get(), get()) }
    single { FollowService(get()) }
    single { PostService(get()) }
    single { LikeService(get(), get(), get()) }
    single { CommentService(get()) }
    single { ActivityService(get(), get(), get()) }

}
package com.prmcoding.di

import com.prmcoding.repository.user.FakeUserRepository
import com.prmcoding.data.repository.user.UserRepository
import org.koin.dsl.module

internal val testModule = module {
    single<UserRepository> { FakeUserRepository() }
}
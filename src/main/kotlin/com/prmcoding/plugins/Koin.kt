package com.prmcoding.plugins

import com.prmcoding.di.mainModule
import io.ktor.server.application.*
import org.koin.core.context.GlobalContext
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    if (GlobalContext.getOrNull() == null) {
        install(Koin) {
            modules(mainModule)
        }
    }


}
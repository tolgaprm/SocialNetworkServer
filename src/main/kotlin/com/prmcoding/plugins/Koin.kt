package com.prmcoding.plugins

import com.prmcoding.di.mainModule
import io.ktor.server.application.*
import org.koin.core.context.GlobalContext
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    if (GlobalContext.getOrNull() == null) {
        install(Koin) {
            slf4jLogger()
            modules(mainModule)
        }
    }


}
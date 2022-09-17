package ru.hadron

import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.hadron.data.checkPasswordForEmail
import ru.hadron.data.collections.User
import ru.hadron.data.registerUser
import ru.hadron.routes.deleteRoute
import ru.hadron.routes.loginRoute
import ru.hadron.routes.registerRoute

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    install(Routing) {
        registerRoute()
        loginRoute()
        deleteRoute()
    }
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    install(Authentication) {
        configureAuth()
    }
//    CoroutineScope(Dispatchers.IO).launch {
//        registerUser(
//            User(
//                "hhh@hhh.ya",
//                "123456"
//            )
//        )
//    }
}

private fun AuthenticationConfig.configureAuth() {
    basic {
        realm = "Porker Server"
        validate {credentials ->
            val email = credentials.name
            val password  = credentials.password
            if (checkPasswordForEmail(email, password)) {
                UserIdPrincipal(email)
            } else null
        }
    }
}

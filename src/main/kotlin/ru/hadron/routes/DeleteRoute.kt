package ru.hadron.routes

import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.hadron.data.collections.User
import ru.hadron.data.deleteUser
import ru.hadron.data.response.SimpleResponse

fun Route.deleteRoute() {
   route("/delete") {
       authenticate {
           post {
               val user = try {
                   call.receive<User>()
               } catch (e: ContentTransformationException) {
                   call.respond(BadRequest)
                   return@post
               }
               if (deleteUser(user.email)) {
                   call.respond(OK, SimpleResponse(true, "account deleted"))
               } else {
                   call.respond(Conflict, SimpleResponse(false, "your have no permissions to delete this account"))
               }
           }
       }

   }
}
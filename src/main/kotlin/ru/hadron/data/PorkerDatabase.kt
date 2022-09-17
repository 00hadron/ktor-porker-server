package ru.hadron.data

import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import ru.hadron.data.collections.User

val client = KMongo.createClient().coroutine
private val database = client.getDatabase("PorkerDatabase")
private val users = database.getCollection<User>()

suspend fun registerUser(user: User): Boolean {
    return users.insertOne(user).wasAcknowledged()
}

suspend fun checkIfUserExist(email: String): Boolean {
    return users.findOne(User::email eq email) != null
}

suspend fun checkPasswordForEmail(email: String, passwordToCheck: String): Boolean {
    val actualPassword = users.findOne(User::email eq email)?.password ?: return false
    return actualPassword == passwordToCheck
}

suspend fun deleteUser(email: String): Boolean {
    val userExist = checkIfUserExist(email)
    return if (userExist) {
        users.deleteOne(User::email eq email).wasAcknowledged()
    } else {
        false
    }
}
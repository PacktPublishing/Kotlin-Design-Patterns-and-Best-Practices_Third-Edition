package com.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    println("open http://localhost:8080")
    embeddedServer(
        CIO, // Can also be Netty
        port = 8080,
        module = Application::myModule
    ).start(wait = true)
}

fun Application.myModule() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/") {
            call.respondText("OK")
        }
        get("/status") {
            call.respond(mapOf("status" to "OK"))
        }
    }
}
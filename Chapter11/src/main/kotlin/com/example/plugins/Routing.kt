package com.example.plugins

import com.example.cats.CatsServiceImpl
import com.example.cats.catsRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val catsService = CatsServiceImpl()
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/status") {
            call.respond(mapOf("status" to "OK"))
        }
        catsRoutes(catsService)
    }
    println("open http://localhost:8080")
}

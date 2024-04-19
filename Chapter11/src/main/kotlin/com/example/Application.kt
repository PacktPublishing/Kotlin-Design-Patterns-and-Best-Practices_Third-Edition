package com.example

import com.example.cats.CatsServiceImpl
import com.example.plugins.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation

fun main() {
    embeddedServer(
        CIO,
        port = 8080,
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {

    DB.connect(environment.config)

    transaction {
        SchemaUtils.create(CatsTable)
    }

    install(ServerContentNegotiation) {
        json()
    }
    configureRouting()
}


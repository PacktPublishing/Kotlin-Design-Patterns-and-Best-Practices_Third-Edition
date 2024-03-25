package com.example.cats

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.catsRoutes(service: CatsService) {
    route("/cats") {
        post {
            val parameters: Parameters = call.receiveParameters()
            val name = requireNotNull(parameters["name"])
            val age = parameters["age"]?.toInt() ?: 0
            service.create(name, age)
            call.respond(HttpStatusCode.Created)
        }
        get {
            val cats = service.findAll()
            call.respond(cats)
        }
        get("/{id}") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            val cat = service.find(id)

            if (cat == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(cat)
            }
        }
        delete("/{id}") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            val deleted = service.delete(id)

            if (deleted == 0) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK)
            }
        }
        put("/{id}") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            val parameters: Parameters = call.receiveParameters()
            val name = requireNotNull(parameters["name"])
            val age = parameters["age"]?.toInt() ?: 0

            val updated = service.update(id, name, age)

            if (updated == 0) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}





package com.example

import io.ktor.server.config.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database

object DB {
    fun connect() = connect(
        host = System.getenv("DB_HOST") ?: "localhost",
        port = System.getenv("DB_PORT")?.toIntOrNull() ?: 5432,
        dbName = System.getenv("DB_NAME") ?: "cats_db",
        dbUser = System.getenv("DB_USER") ?: "cats_admin",
        dbPassword = System.getenv("DB_PASSWORD") ?: "abcd1234"
    )

    fun connect(config: ApplicationConfig) = connect(
        host = config.property("db.host").getString(),
        port = config.property("db.port").getString().toInt(),
        dbName = config.property("db.dbName").getString(),
        dbUser = config.property("db.dbUser").getString(),
        dbPassword = config.property("db.dbPassword").getString()
    )

    private fun connect(
        host: String,
        port: Int,
        dbName: String,
        dbUser: String,
        dbPassword: String
    ) = Database.connect(
        url = "jdbc:postgresql://$host:$port/$dbName",
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword,
    )
}

object CatsTable : IntIdTable() {
    val name = varchar("name", 20).uniqueIndex()
    val age = integer("age").default(0)
}

@Serializable
data class 20Cat(
    val id: Int,
    val name: String,
    val age: Int
)
import cats.CatsServiceImpl
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
        module = Application::mainModule
    ).start(wait = true)
}

fun Application.mainModule() {
    DB.connect()

    transaction {
        SchemaUtils.create(CatsTable)
    }

    install(ServerContentNegotiation) {
        json()
    }
    val catsService = CatsServiceImpl()
    routing {
        get("/status") {
            call.respond(mapOf("status" to "OK"))
        }
        cats(catsService)
    }
    println("open http://localhost:8080")
}


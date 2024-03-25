import com.example.myModule
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {

    @Test
    fun testStatus() {
        testApplication {
            application {
                myModule()
            }

            val response = client.get("/status")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("""{"status":"OK"}""", response.bodyAsText())
        }
    }
}
import com.example.CatsTable
import com.example.DB
import com.example.module
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServerTest {
    @AfterAll
    fun cleanup() {
        DB.connect()
        transaction {
            SchemaUtils.drop(CatsTable)
        }
    }

    @BeforeAll
    fun setup() {
        DB.connect()
        transaction {
            SchemaUtils.create(CatsTable)
        }
    }

    @Test
    fun testStatus() {
        testApplication {
            application {
                module()
            }
            val response = client.get("/status")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("""{"status":"OK"}""", response.bodyAsText())
        }
    }

    @Test
    fun `POST creates a new cat`() {
        testApplication {
            application {
                module()
            }
            val response = client.post("/cats") {
                header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(
                    listOf(
                        "name" to "Meatloaf",
                        "age" to 4.toString()
                    ).formUrlEncode()
                )
            }
            assertEquals(HttpStatusCode.Created, response.status)
        }
    }

    @Nested
    inner class `With cat in DB` {
        @Test
        fun `GET with ID fetches a single cat`() {
            testApplication {
                application {
                    module()
                }
                val response = client.get("/cats/$id")
                assertEquals("""{"id":$id,"name":"Fluffy","age":2}""", response.bodyAsText())
            }
        }

        private lateinit var id: EntityID<Int>

        @BeforeEach
        fun setup() {
            DB.connect()
            id = transaction {
                CatsTable.insertAndGetId { cat ->
                    cat[name] = "Fluffy"
                    cat[age] = 2
                }
            }
        }

        @AfterEach
        fun teardown() {
            DB.connect()
            transaction {
                CatsTable.deleteAll()
            }
        }


        @Test
        fun `GET without ID fetches all cats`() {
            testApplication {
                application {
                    module()
                }
                val response = client.get("/cats")
                assertEquals("""[{"id":$id,"name":"Fluffy","age":2}]""", response.bodyAsText())
            }
        }

        @Test
        fun `DELETE deletes a cat`() {
            testApplication {
                application {
                    module()
                }
                val response = client.delete("/cats/$id")
                assertEquals(HttpStatusCode.OK, response.status)

                val deletedResponse = client.get("/cats/$id")
                assertEquals(HttpStatusCode.NotFound, deletedResponse.status)
            }
        }

        @Test
        fun `PUT updates a cat`() {
            testApplication {
                application {
                    module()
                }
                val response = client.put("/cats/$id") {
                    header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                    setBody(
                        listOf(
                            "name" to "Meatloaf",
                            "age" to 4.toString()
                        ).formUrlEncode()
                    )
                }
                assertEquals(HttpStatusCode.OK, response.status)

                val updatedResponse = client.get("/cats/$id")
                assertEquals("""{"id":$id,"name":"Meatloaf","age":4}""", updatedResponse.bodyAsText())
            }
        }
    }
}
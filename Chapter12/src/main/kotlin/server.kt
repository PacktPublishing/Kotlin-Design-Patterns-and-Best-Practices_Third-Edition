import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle

fun main() {
    val vertx = Vertx.vertx()
    vertx.deployVerticle(ServerVerticle())
    vertx.deployVerticle(CatsVerticle())
}

class ServerVerticle : CoroutineVerticle() {
    override suspend fun start() {
        val router = router()

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8081)
        println("open http://localhost:8081/status")
    }

    private fun router(): Router = Router.router(vertx).apply {
        route().handler(BodyHandler.create())
        get("/status").handler { ctx ->
            val json = json {
                obj(
                    "status" to "OK"
                )
            }

            ctx.response()
                .setStatusCode(200)
                .end(json.toString())
        }
        route("/cats/*").subRouter(catsRouter())
    }

    private fun catsRouter(): Router = Router.router(vertx).apply {
        delete("/:id").handler { ctx ->
            val id = ctx.request().getParam("id").toInt()
            vertx.eventBus().request<Int>("cats:delete", id) {
                ctx.end()
            }
        }
        put("/:id").handler { ctx ->
            val id = ctx.request().getParam("id").toInt()
            val body = ctx.body().asJsonObject()
            // This code is extracted
            /*
            db.preparedQuery("UPDATE cats SET name = $1, age = $2 WHERE ID = $3")
                .execute(
                    Tuple.of(
                        body.getString("name"),
                        body.getInteger("age"),
                        id
                    )
                ).await()
            */
            ctx.end()
        }
    }
}



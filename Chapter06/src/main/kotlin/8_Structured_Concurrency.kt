import kotlinx.coroutines.*
import java.util.*
import kotlin.RuntimeException

fun main() {
    withSupervisorScope()
    withSupervisorScopeAndExceptionHandler()
    withCoroutineScope()
}

fun withSupervisorScopeAndExceptionHandler() = runBlocking {
    println("Running with Supervisor Scope and Exception Handler")
    val exceptionHandler = CoroutineExceptionHandler { _, e ->
        println("Exception: $e")
    }
    val parent = supervisorScope {
        val children = List(10) { childId ->
            launch(exceptionHandler) {
                for (i in 1..1_000_000) {
                    UUID.randomUUID()

                    if (i % 100_000 == 0) {
                        println("$childId - $i")
                        yield()
                    }
                    if (childId == 8 && i == 300_000) {
                        throw RuntimeException("Something bad happened")
                    }
                }
            }
        }
    }
}

fun withSupervisorScope() = runBlocking {
    println("Running with Supervisor Scope")
    val parent = launch(Dispatchers.Default) {
        supervisorScope {
            val children = List(10) { childId ->
                launch {
                    for (i in 1..1_000_000) {
                        UUID.randomUUID()

                        if (i % 100_000 == 0) {
                            println("$childId - $i")
                            yield()
                        }
                        if (childId == 8 && i == 300_000) {
                            throw RuntimeException("Something bad happened")
                        }
                    }
                }
            }
        }
    }
}

fun withCoroutineScope() = runBlocking {
    println("Running with Coroutine Scope")
    val parent = launch(Dispatchers.Default) {
        coroutineScope {
            val children = List(10) { childId ->
                launch {
                    for (i in 1..1_000_000) {
                        UUID.randomUUID()

                        if (i % 100_000 == 0) {
                            println("$childId - $i")
                            yield()
                        }
                        if (childId == 8 && i == 300_000) {
                            throw RuntimeException("Something bad happened")
                        }
                    }
                }
            }
        }
    }
}

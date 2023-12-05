import arrow.resilience.CircuitBreaker
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

suspend fun main() {
    var circuitBreaker = CircuitBreaker(
        openingStrategy = CircuitBreaker.OpeningStrategy.Count(1),
        resetTimeout = 1.seconds,
        exponentialBackoffFactor = 2.0,
        maxResetTimeout = 60.seconds,
    )

    circuitBreaker = circuitBreaker.doOnHalfOpen {
        println("Half Open!")
    }.doOnOpen {
        println("Open!")
    }.doOnClosed {
        println("Closed!")
    }

    remoteServer(0.3).forEach { req ->
        try {
            delay(400L)
            circuitBreaker.protectOrThrow { req() }.also {
                println("Response: $it")
            }
        } catch (e: RuntimeException) {
            println("Server returned exception: $e")
        } catch (e: CircuitBreaker.ExecutionRejected) {
            println("Circuit breaker exception: ${e.reason}")
        }
    }
}

fun remoteServer(failureChance: Double) = sequence {
    while (true) {
        if (Random.nextDouble(1.0) < failureChance) {
            yield { throw RuntimeException() }
        } else {
            yield { "OK" }
        }
    }
}


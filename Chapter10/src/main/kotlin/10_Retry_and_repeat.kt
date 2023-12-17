import arrow.resilience.Schedule
import arrow.resilience.retry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlin.time.Duration.Companion.seconds

suspend fun main() {
    retryExample()

    val successThenFailure = sequence {
        yield { "OK" }
        yield { "OK" }
        yield { "OK" }
        while (true) {
            yield { throw RuntimeException() }
        }
    }.iterator()

    Schedule.recurs<Unit>(10).repeatOrElse({
        println(successThenFailure.next()())
    }, { t, l -> println("Failed on attempt $l with $t"); -1 })
}

suspend fun retryExample() {
    val responses = serverResponses().retry(
        Schedule.recurs<Throwable>(10)
            .and(Schedule.exponential(1.seconds))
    ).toList()
    println(responses)
}


fun serverResponses(): Flow<String> {
    var requests = 0
    var lastErrorTime = System.currentTimeMillis()

    return flow {
        if (requests++ < 3) {
            println("Error occured at ${System.currentTimeMillis() - lastErrorTime}")
            lastErrorTime = System.currentTimeMillis()
            throw RuntimeException("Something went wrong")
        } else {
            emit("OK")
        }
    }
}


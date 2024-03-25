import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

suspend fun main() {
    catchingExceptions()
    onCompletion()
    flowRetry()
    flowRetryWhen()
}

suspend fun flowRetryWhen() {
    flow {
        repeat(3) {
            emit(doSomethingHttpRisky()) // This might throw an exception
        }
    }.retryWhen { e: Throwable, attempts: Long ->
        println("Got $e, retrying")
        when {
            (e is Http5XX) && attempts > 10 -> false
            (e is Http4XX) && attempts > 3 -> false
            else -> true
        }


    }.collect { println(it) }
}

class Http5XX(message: String) : Throwable(message)
class Http4XX(message: String) : Throwable(message)

fun doSomethingHttpRisky(): Int {
    val randomNumber = Random.nextInt(10)
    if (randomNumber < 2) {
        throw Http5XX(randomNumber.toString())
    } else if (randomNumber < 5) {
        throw Http4XX(randomNumber.toString())
    }
    return randomNumber
}


suspend fun flowRetry() {
    flow {
        repeat(3) {
            emit(doSomethingRisky()) // This might throw an exception
        }
    }
        // Number of retry attempts
        .retry(10)
        // Here you can check what type of exception you received
        { e: Throwable ->
            println("Got $e, retrying")
            true
        }
        .collect { println(it) }
}

fun doSomethingRisky(): Int {
    val randomNumber = Random.nextInt(10)
    if (randomNumber > 4) {
        throw RuntimeException(randomNumber.toString())
    }
    return randomNumber
}

fun onCompletion() = runBlocking {
    flow {
        emit(1)
        emit(2)
    }
        .onCompletion { cause ->
            if (cause != null) {
                println("Flow completed with exception: $cause")
            } else {
                println("Flow completed successfully")
            }
        }
        .collect { println(it) }
}

fun catchingExceptions() = runBlocking {
    flow {
        var i = 3
        repeat(5) {
            emit(10 / i--)
        }
    }
        .catch { e -> println("Caught exception: $e") }
        .collect { value -> println(value) }
}

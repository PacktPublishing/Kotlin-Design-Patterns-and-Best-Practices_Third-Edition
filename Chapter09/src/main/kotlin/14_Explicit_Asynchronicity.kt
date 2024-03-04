import kotlinx.coroutines.*

fun main() {
    runBlocking {
        // Prints DeferredCoroutine{Active}
        println("Result: ${getResult()}")

        // Prints "OK"
        println("Result: ${getResultAsync().await()}")
    }
}

// This will produce a warning
fun CoroutineScope.getResult(): Deferred<String> = async {
    delay(100)
    "OK"
}

fun CoroutineScope.getResultAsync() = async {
    delay(100)
    "OK"
}
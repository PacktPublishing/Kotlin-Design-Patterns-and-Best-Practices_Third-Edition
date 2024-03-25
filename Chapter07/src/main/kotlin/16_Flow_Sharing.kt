import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

suspend fun main() {
   // sharedFlowExample()
    stateFlowExample()
}

fun stateFlowExample() = runBlocking {
    val originalFlow = flowOf(1, 2, 3, 4, 5)  .onEach { println("Sending $it from ${Thread.currentThread().name}") }
    val stateFlow = originalFlow.stateIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(),
        initialValue = 0
    )


repeat(5) { id ->
    launch(Dispatchers.Default) {
        stateFlow.map { value ->
            println("Coroutine $id got $value on ${Thread.currentThread().name}")
        }.collect()

    }
    delay(100L)
}
}

fun sharedFlowExample() = runBlocking {
val originalFlow = flowOf(1, 2, 3, 4, 5)
    .onEach { println("Sending $it from ${Thread.currentThread().name}") }

val sharedFlow = originalFlow
    .shareIn(
        scope = CoroutineScope(newFixedThreadPoolContext(4, "my dispatcher")),
        started = SharingStarted.Lazily,
        replay = 2
    )


repeat(5) { id ->
    launch(Dispatchers.Default) {
        sharedFlow.map { value ->
            println("Coroutine $id got $value on ${Thread.currentThread().name}")
        }.collect()

    }
    delay(100L)
}
}
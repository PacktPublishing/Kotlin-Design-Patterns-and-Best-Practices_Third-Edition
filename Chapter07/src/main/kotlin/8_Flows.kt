import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    flowsAndExceptions()
    flowOnExample()
}

fun flowOnExample() =
runBlocking {
    val moreNumbersFlow = (1..10).asFlow()

    moreNumbersFlow.map {
        println("Mapping on ${Thread.currentThread().name}")
        it * it
    }.flowOn(Dispatchers.Default).collect {
        println("Got $it on ${Thread.currentThread().name}")
    }
}

fun flowsAndExceptions() {
    runBlocking {
        val numbersFlow: Flow<Int> = flow {
            println("New subscriber!")
            (1..10).forEach {
                println("Sending $it")
                emit(it)
                if (it == 9) {
                    throw RuntimeException()
                }
            }
        }



        (1..4).forEach { coroutineId ->
            delay(5000)
            launch(Dispatchers.Default) {
                try {
                    numbersFlow.collect { number ->
                        delay(1000)
                        println("Coroutine $coroutineId received $number")
                    }
                } catch (e: Exception) {
                    println("Coroutine $coroutineId got an error")
                }
            }
        }

    }
}

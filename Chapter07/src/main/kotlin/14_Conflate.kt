import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val stock: Flow<Int> = flow {
            var i = 0
            while (true) {
                emit(++i)
                delay(100)
            }
        }

        var seconds = 0
        stock.conflate().collect { number ->
            delay(1000)
            seconds++
            println("$seconds seconds -> received $number")
        }

        val debouncedFlow = stock.debounce(300L)

        val sampleFlow = stock.sample(300L)

    }
}
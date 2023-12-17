import arrow.fx.coroutines.CyclicBarrier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val barrier = CyclicBarrier(3)

    runBlocking(Dispatchers.IO) {
        ('a'..'z').forEach { letter ->
            launch {
                barrier.await()
                fetchAsync("https://en.wikipedia.org/wiki/$letter")
                println("Fetched letter $letter at ${System.currentTimeMillis() % 1000}")
            }
        }
    }
}
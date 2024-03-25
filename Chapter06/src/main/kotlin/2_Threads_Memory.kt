import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.system.exitProcess

fun main() {
    threadsMemory()
    System.gc()
    coroutinesMemory()
}

fun threadsMemory() {
    val counter = AtomicInteger()

    val threads: List<Thread> = try {
        List(10_000) {
            thread {
                Thread.sleep(1000)
                counter.incrementAndGet()
            }
        }
    } catch (oome: OutOfMemoryError) {
        println("Spawned ${counter.get()} threads before crashing")
        exitProcess(-42)
    }

    threads.forEach {
        it.join()
    }
    println(
        "Finished ${counter.get()} with threads without running Out of Memory consuming ${
            (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024
        }Mb"
    )
}

fun coroutinesMemory() = runBlocking {
    val counter = AtomicInteger()

    val coroutines = List(10_000) {
        launch {
            delay(1000)
            counter.incrementAndGet()
        }
    }

    coroutines.forEach {
        it.join()
    }

    println(
        "Finished ${counter.get()} with coroutines without running Out of Memory consuming ${
            (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024
        }Mb"
    )
}

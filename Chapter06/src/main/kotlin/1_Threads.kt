import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

fun main() {
    startingThreads()
    threadRace()
    synchronisingThreads()
    daemonThread()
}

fun daemonThread() {
    thread(isDaemon = true) {
        for (i in 1..1_000_000) {
            println("daemon thread says: $i")
        }
    }
    Thread.sleep(10)
}

fun synchronisingThreads() {
    var counter = 0
    val latch = CountDownLatch(100_000)
    repeat(100) {
        thread {
            repeat(1000) {
                synchronized(latch) {
                    counter++
                    latch.countDown()
                }
            }
        }
    }

    latch.await()

    println("Counter $counter")
}

fun threadRace() {
    var counter = 0
    val latch = CountDownLatch(100_000)
    repeat(100) {
        thread {
            repeat(1000) {
                counter++
                latch.countDown()
            }
        }
    }

    latch.await()

    println("Counter $counter")
}

fun startingThreads() {
    repeat(2) { t ->
        thread {
            for (i in 1..100) {
                println("T$t: $i")
            }
        }
    }
}

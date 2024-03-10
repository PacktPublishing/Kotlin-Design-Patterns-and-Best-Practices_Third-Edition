import arrow.resilience.saga
import arrow.resilience.transact
import java.lang.Exception

suspend fun main() {
    val box = DonutBoxSaga()
    val sendDonutsSaga = saga {
        saga({
            putDonuts(box)
        }) {
            unpack(box)
        }
        saga({
            addLabel(box)
        }) {
            removeLabel(box)
        }
        saga({
            passToCourier(box)
        }) {
            println("I wasted so much time and the courier never came!")
        }
    }
    try {
        sendDonutsSaga.transact()
    } catch (e: Exception) {
        println("Failed saga: ${e.message}")
    }
}

fun passToCourier(box: DonutBoxSaga) {
    throw RuntimeException("Courier never came!")
    println("Box passed to the courier")
}

fun removeLabel(box: DonutBoxSaga) {
    println("Removing the label")
}

fun addLabel(box: DonutBoxSaga) {
    println("Adding label to the box")
}

fun unpack(box: DonutBoxSaga) {
    println("Putting donuts back on the counter")
}

fun putDonuts(box: DonutBoxSaga) {
    println("Putting donuts in a box")
}

class DonutBoxSaga {

}

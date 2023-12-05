import kotlin.random.Random

fun main() {
    var status: PizzaOrderStatus = OrderReceived(123)
    while (status !is Completed) {
        status = status.nextStatus()
        println(status)
    }
}


// Java-like code that uses enum to represent State
/*enum class PizzaOrderStatus {
    ORDER_RECEIVED, PIZZA_BEING_MADE, OUT_FOR_DELIVERY, COMPLETED;

    fun nextStatus(): PizzaOrderStatus {
        return when (this) {
            ORDER_RECEIVED -> PIZZA_BEING_MADE
            PIZZA_BEING_MADE -> OUT_FOR_DELIVERY
            OUT_FOR_DELIVERY -> COMPLETED
            COMPLETED -> COMPLETED
        }
    }
}*/

sealed interface PizzaOrderStatus {
    val orderId: Int
    fun nextStatus(): PizzaOrderStatus
}


data class OrderReceived(override val orderId: Int) : PizzaOrderStatus {
    override fun nextStatus() = PizzaBeingMade(orderId)
}

data class PizzaBeingMade(override val orderId: Int) : PizzaOrderStatus {
    override fun nextStatus() = OutForDelivery(orderId)
}

data class OutForDelivery(override val orderId: Int) : PizzaOrderStatus {
    override fun nextStatus() = Completed(orderId)
}

data class Completed(override val orderId: Int) : PizzaOrderStatus {
    override fun nextStatus() = this
}
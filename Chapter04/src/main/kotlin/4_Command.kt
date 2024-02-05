fun main() {
    val t = Trooper()

    t.addOrder(moveGenerator(t, 1, 1))
    t.addOrder(moveGenerator(t, 2, 2))
    t.addOrder(moveGenerator(t, 3, 3))

    t.executeOrders()

    t.appendMove(0, 4)
    .appendMove(5, 4)
    .appendMove(5, 8)
    .appendMove(10, 8)
    .executeOrders()
}

open class Trooper {
    private val orders = mutableListOf<Command>()
    private val undoableOrders = mutableListOf<Pair<Command, Command>>()

    fun addOrder(order: Command) {
        this.orders.add(order)
    }

    fun executeOrders() {
        while (orders.isNotEmpty()) {
            val order = orders.removeFirst()
            order() // Compile error for now
        }
    }

    fun appendMove(x: Int, y: Int): Trooper = apply {

        orders.add(moveGenerator(this, x, y))

        undoableOrders.add(moveGenerator(this, x, y) to moveGenerator(this, -x, -y))

    }

    // More code here

    fun move(x: Int, y: Int) {
        println("Moving to $x:$y")
    }
}

typealias Command = () -> Unit

val moveGenerator = fun(
    t: Trooper,
    x: Int,
    y: Int
): Command {
    return fun() {
        t.move(x, y)
    }
}
import arrow.core.Either
import arrow.core.left
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.fold
import arrow.core.right

fun main() {
    //  withNulls()
    // withRaise()
    withEither()
}

fun withEither() {
    val box = DonutBoxEither(1)

    box.addDonut(
        Donut(
            "TONGAN VANILLA BEAN CUSTARD",
            listOf("Milk", "Wheat"),
            1000
        )
    )

    when (val result = box.removeDonut("SRI LANKAN CINNAMON SUGAR")) {
        is Either.Left -> println("No donut for me")
        is Either.Right -> println("I've got ${result.value.name}")
    }
}

fun withRaise() {
    val box = DonutBoxRaise(1)

    box.addDonut(
        Donut(
            "TONGAN VANILLA BEAN CUSTARD",
            listOf("Milk", "Wheat"),
            1000
        )
    )

    fold(
        {
            removeDonut(box, "SRI LANKAN CINNAMON SUGAR")
        },
        { _: NoSuchDonut ->
            println("No donut for me")
        },
        { donut: Donut ->
            println("I've got ${donut.name}")
        }
    )
}

fun withNulls() {
    val box = DonutBox(1)
    box.addDonut(
        Donut(
            "TONGAN VANILLA BEAN CUSTARD",
            listOf("Milk", "Wheat"),
            1000
        )
    )

    val donut = box.removeDonut("SRI LANKAN CINNAMON SUGAR")
    if (donut != null) {
        println("I've got ${donut.name}")
    } else {
        println("No donut for me")
    }
}

data class Donut(
    val name: String,
    val allergens: List<String>,
    val calories: Int
)

class DonutBox(private val capacity: Int) {
    private val donuts = mutableListOf<Donut>()
    fun addDonut(donut: Donut): DonutBox = apply {
        if (donuts.size < capacity) {
            donuts.add(donut)
        } else {
            throw RuntimeException("No space in the box")
        }
    }

    fun removeDonut(name: String): Donut? {
        val donutIndex = donuts.indexOfFirst { it.name == name }
        if (donutIndex >= 0) {
            return donuts.removeAt(donutIndex)
        }

        return null
    }
}

class DonutBoxRaise(private val capacity: Int) {
    val donuts = mutableListOf<Donut>()

    fun addDonut(donut: Donut): DonutBoxRaise {
        if (donuts.size < capacity) {
            donuts.add(donut)
            return this
        } else {
            throw RuntimeException("No space in the box")
        }
    }
}

data class NoSuchDonut(val name: String)

fun Raise<NoSuchDonut>.removeDonut(
    box: DonutBoxRaise,
    name: String
): Donut {
    val donutIndex = box.donuts.indexOfFirst { it.name == name }
    if (donutIndex >= 0) {
        return box.donuts.removeAt(donutIndex)
    }

    raise(NoSuchDonut(name))
}

class DonutBoxEither(private val capacity: Int) {
    private val donuts = mutableListOf<Donut>()
    fun addDonut(donut: Donut) = apply {
        if (donuts.size < capacity) {
            donuts.add(donut)
        } else {
            throw RuntimeException("No space in the box")
        }
    }

    fun removeDonut(name: String): Either<NoSuchDonut, Donut> = either {
        val donutIndex = donuts.indexOfFirst { it.name == name }
        return if (donutIndex >= 0) {
            (donuts.removeAt(donutIndex)).right()
        } else {
            (NoSuchDonut(name)).left()
        }
    }
}
import arrow.core.*
import arrow.core.raise.*
import java.util.*

fun main() {
    withNullsAndExceptions()
    withEither()
    withRaise()
    withResult()
}

fun withEither() {
    when (val box = DonutBoxEither(1)) {
        is Either.Left -> println("Couldn't construct a box")
        is Either.Right -> {
            val validBox = box.value
            when (validBox.addDonut(
                Donut(
                    "TONGAN VANILLA BEAN CUSTARD",
                    1000,
                    listOf("Milk", "Wheat")
                )
            )) {
                is Either.Left -> println("No space in box")
                is Either.Right -> when (val result = validBox.removeDonut("SRI LANKAN CINNAMON SUGAR")) {
                    is Either.Left -> println("No donut for me")
                    is Either.Right -> println("I've got ${result.value.name}")
                }
            }
        }
    }
}

fun withResult() {
    val box = DonutBoxResult(1)

    val result = box.addDonut(
        Donut(
            "TONGAN VANILLA BEAN CUSTARD",
            1000,
            listOf("Milk", "Wheat")
        )
    )

    result.fold(
        onFailure = { println("No space in the box") },
        onSuccess = {
            val donutResult = box.removeDonut("SRI LANKAN CINNAMON SUGAR")
            donutResult.fold(
                onSuccess = { donut ->
                    println("I've got ${donut.name}")
                },
                onFailure = { println("No donut for me") }
            )
        }
    )
}

class NoSpaceInBoxException : RuntimeException("No space in box")

fun withRaise() {
    val box = DonutBoxRaise(1)

    fold(
        {
            addDonut(
                box,
                Donut(
                    "TONGAN VANILLA BEAN CUSTARD",
                    1000,
                    listOf("Milk", "Wheat"),
                )
            )
        }, {
            println("No space in box")
        }, {
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
    )
}

fun withNullsAndExceptions() {
    val box = DonutBox(1)

    try {
        box.addDonut(
            Donut(
                "TONGAN VANILLA BEAN CUSTARD",
                1000,
                listOf("Milk", "Wheat"),
            )
        )

        val donut = box.removeDonut("SRI LANKAN CINNAMON SUGAR")
        if (donut != null) {
            println("I've got ${donut.name}")
        } else {
            println("No donut for me")
        }
    } catch (e: NoSpaceInBoxException) {
        println(e.message)
    }
}

data class Donut(
    val name: String,
    val calories: Int,
    val allergens: List<String> = listOf()
)

class DonutBox(private val capacity: Int) {
    private val donuts = mutableListOf<Donut>()
    fun addDonut(donut: Donut): DonutBox = apply {
        if (donuts.size < capacity) {
            donuts.add(donut)
        } else {
            throw NoSpaceInBoxException()
        }
    }

    fun removeDonut(name: String): Donut? {
        return donuts.find { it.name == name }?.let {
            donuts.remove(it)
            it
        }
    }
}

class DonutBoxOptional(private val capacity: Int) {
    private val donuts = mutableListOf<Donut>()
    fun addDonut(donut: Donut): DonutBoxOptional = apply {
        if (donuts.size < capacity) {
            donuts.add(donut)
        } else {
            throw NoSpaceInBoxException()
        }
    }

    fun removeDonut(name: String): Optional<Donut> {
        return donuts.find { it.name == name }?.let {
            donuts.remove(it)
            Optional.of(it)
        } ?: Optional.empty()
    }
}


data class NoSuchDonut(val name: String)


sealed interface BoxError
data object NoSpaceInBox : BoxError
data object AlmostNoSpaceInBox : BoxError

data class DonutBoxRaise(
    val capacity: Int,
    val donuts: MutableList<Donut> = mutableListOf(),
)

fun Raise<NoSpaceInBox>.addDonut(
    donutBox: DonutBoxRaise,
    donut: Donut
): DonutBoxRaise {
    if (donutBox.donuts.size < donutBox.capacity) {
        donutBox.donuts.add(donut)
        return donutBox
    } else {
        raise(NoSpaceInBox)
    }
}

fun Raise<NoSuchDonut>.removeDonut(
    box: DonutBoxRaise,
    name: String
): Donut {
    box.donuts.find { it.name == name }?.let {
        box.donuts.remove(it)
        return it
    }

    raise(NoSuchDonut(name))
}

data class NonPositiveCapacity(val capacity: Int)

class DonutBoxEither private constructor(private val capacity: Int) {
    companion object {
        operator fun invoke(capacity: Int): Either<NonPositiveCapacity, DonutBoxEither> = either {
            ensure(capacity > 0) { NonPositiveCapacity(capacity) }
            DonutBoxEither(capacity)
        }
    }

    private val donuts = mutableListOf<Donut>()

    fun addDonut(donut: Donut): Either<NoSpaceInBox, DonutBoxEither> =
        either {
            ensure(
                donuts.size <= capacity
            ) { NoSpaceInBox }
            donuts.add(donut)
            this@DonutBoxEither
        }

    fun removeDonut(name: String): Either<NoSuchDonut, Donut> {
        return donuts.find { it.name == name }?.let {
            donuts.remove(it)
            return it.right()
        } ?: NoSuchDonut(name).left()
    }
}

class DonutBoxIor(private val capacity: Int) {
    private val donuts = mutableListOf<Donut>()

    fun addDonut(donut: Donut): Ior<BoxError, DonutBoxIor> =
        ior(combineError = { _, both -> both }) {
            ensure(
                donuts.size <= capacity
            ) { NoSpaceInBox }
            donuts.add(donut)

            return if (donuts.size == capacity) {
                (AlmostNoSpaceInBox to this@DonutBoxIor).bothIor()
            } else {
                (this@DonutBoxIor).rightIor()
            }
        }

    fun removeDonut(name: String): Ior<NoSuchDonut, Donut> {
        return donuts.find { it.name == name }?.let {
            donuts.remove(it)
            return (NoSuchDonut(name) to it).bothIor()
        } ?: NoSuchDonut(name).leftIor()
    }
}

class DonutBoxResult(private val capacity: Int) {
    private val donuts = mutableListOf<Donut>()

    fun addDonut(donut: Donut): Result<DonutBoxResult> =
        if (donuts.size <= capacity) {
            donuts.add(donut)
            Result.success(this)
        } else {
            Result.failure(
                NoSpaceInBoxException()
            )
        }

    fun removeDonut(name: String): Result<Donut> {
        return donuts.find { it.name == name }?.let {
            donuts.remove(it)
            Result.success(it)
        } ?: Result.failure(NoSuchDonutException())
    }
}

class NoSuchDonutException : Throwable()

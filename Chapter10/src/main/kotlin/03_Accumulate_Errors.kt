import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.mapOrAccumulate
import arrow.core.raise.*

fun main() {
    val box = DonutBoxRaise(2)
    // Add donuts to the box
    fold({
        addDonut(box, Donut("TONGAN VANILLA BEAN CUSTARD", 1000, listOf("Wheat", "Milk")))
        addDonut(box, Donut("SRI LANKAN CINNAMON SUGAR", 800, listOf("Wheat")))
    }, {}, {})

    mapAndAccumulate(box)
    zipAndAccumulate(box)
}


fun mapAndAccumulate(box: DonutBoxRaise) {
    println("mapAndAccumulate: ")
    val res: Either<NonEmptyList<DonutIssue>, List<Donut>> =
        box.donuts.mapOrAccumulate { caloriesChecker(700, it) }

    when (res) {
        is Either.Left -> res.value.forEach {
            println(it)
        }

        is Either.Right -> println("All donuts should be fine!")
    }
}

fun zipAndAccumulate(box: DonutBoxRaise) {
    println("zipAndAccumulate: ")
    val res: List<Either<NonEmptyList<DonutIssue>, Donut>> =
        box.donuts.map {
            either {
                zipOrAccumulate(
                    { caloriesChecker(700, it) },
                    { allergensChecker(setOf("Milk"), it) }
                ) { _, _ -> it }
            }
        }

    res.forEach { donutCheck ->
        when (donutCheck) {
            is Either.Left -> donutCheck.value.forEach {
                println(it)
            }

            is Either.Right -> println("All checks passed, you can eat ${donutCheck.value.name}!")
        }
    }
}

fun Raise<DonutIssue.TooManyCalories>.caloriesChecker(maxCalories: Int, donut: Donut): Donut {
    ensure(donut.calories < maxCalories)
    { DonutIssue.TooManyCalories(maxCalories, donut.calories) }

    return donut
}

fun Raise<DonutIssue.AllergensPresent>.allergensChecker(allergicTo: Set<String>, donut: Donut): Donut {
    val presentAllergens = donut.allergens.intersect(allergicTo)
    ensure(presentAllergens.isEmpty())
    { DonutIssue.AllergensPresent(presentAllergens) }

    return donut
}

sealed interface DonutIssue {
    data class AllergensPresent(val allergensList: Set<String>) : DonutIssue {
        override fun toString(): String =
            "Allergens present: $allergensList"
    }

    data class TooManyCalories(val max: Int, val given: Int) : DonutIssue {
        override fun toString(): String =
            "Calories $given above maximum $max"
    }
}

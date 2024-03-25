import arrow.core.raise.Raise
import arrow.core.raise.ensure
import arrow.core.raise.fold

fun main() {
    val box = DonutBoxRaise(1)

    fold(
        // Computation block
        {
            box.addDonut(
                Donut(
                    "TONGAN VANILLA BEAN CUSTARD",
                    1000,
                    listOf("Milk", "Wheat"),
                )
            )
        },
        catch = {

        },
        // Failure block
        { _: NoSpaceInBox ->
            println("No space in box")
        },
        // Success block
        {
            fold(
                {
                    box.removeDonut("SRI LANKAN CINNAMON SUGAR")
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

context(Raise<NoSpaceInBox>)
fun DonutBoxRaise.addDonut(
    donut: Donut
): DonutBoxRaise {
    ensure(donuts.size <= capacity) {
        NoSpaceInBox
    }
    donuts.add(donut)
    return this
}

context(Raise<NoSuchDonut>)
fun DonutBoxRaise.removeDonut(
    name: String
): Donut {
    val donutIndex = donuts.indexOfFirst { it.name == name }
    if (donutIndex >= 0) {
        return donuts.removeAt(donutIndex)
    }

    raise(NoSuchDonut(name))
}
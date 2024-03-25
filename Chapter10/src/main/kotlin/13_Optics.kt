import arrow.optics.Every
import arrow.optics.Lens
import arrow.optics.dsl.every
import arrow.optics.optics

fun main() {

    val donutBox = DonutBoxOptics(
        listOf(
            DonutOptics("Gianduja Chocolate & Hazelnut Donut", 2000, allergens = listOf("Milk", "Nuts")),
            DonutOptics("Ginger, Blackberry & Pear Donut", 1500, allergens = listOf("Milk"))
        )
    )

    val donutBoxCorrectAllergensCopy = donutBox.copy(
        donuts = donutBox.donuts.map { donut ->
            donut.copy(allergens = donut.allergens + "Wheat")
        }
    )

    println(donutBoxCorrectAllergensCopy)

    val donutBoxCorrectAllergensOptics = DonutBoxOptics.donuts.modify(donutBox) { donuts ->
        donuts.map { donut ->
            DonutOptics.allergens.modify(donut) { allergens -> allergens + "Wheat" }
        }
    }

    println(donutBoxCorrectAllergensOptics)

    val donutBoxAllergensOptics =
        DonutBoxOptics.donuts.every(Every.list()) compose DonutOptics.allergens

    println(donutBoxAllergensOptics.modify(donutBox) { it + "Wheat" })

}

@optics
data class DonutBoxOptics(val donuts: List<DonutOptics>) {
    companion object
}

fun DonutOptics.addAllergen(allergen: String): DonutOptics =
    DonutOptics.allergens.every(Every.list()).modify(this) { it + allergen }

data class DonutOptics(
    val name: String,
    val calories: Int,
    val allergens: List<String> = listOf()
) {
    companion object {
        val name: Lens<DonutOptics, String> = Lens(
            get = { donut -> donut.name },
            set = { donut, name -> donut.copy(name = name) }
        )
        val calories: Lens<DonutOptics, Int> = Lens(
            get = { donut -> donut.calories },
            set = { donut, calories -> donut.copy(calories = calories) }
        )
        val allergens: Lens<DonutOptics, List<String>> = Lens(
            get = { donut -> donut.allergens },
            set = { donut, allergens -> donut.copy(allergens = allergens) }
        )


    }
}
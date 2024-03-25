import kotlinx.coroutines.runBlocking

fun main() {
    reduce()
    fold()
    scan()
}

fun reduce() {
    val numbers = 1..100
    reduceImperative(numbers)
    reduceReactive(numbers)
}

fun reduceReactive(numbers: IntRange) {
    val reduced: Int = (1..100).reduce { sum, n -> sum + n }
    println("reduced: $reduced")

    val concatenated = listOf("Hello", "Kotlin", "!").reduce { agg, s -> "$agg $s" }
    println(concatenated)

    val factorial = (1..5).reduce { product, n -> product * n}
    println(factorial)

    (1..5).forEach {  }
}

fun fold() {
    val folded: Int = (1..100).fold(10) { sum, n -> sum + n }
    println("folded: $folded")

    val foldedLong: Long = (1..15).fold(1) { acc, n -> acc * n }
    println("foldedLong: $foldedLong")
}

fun reduceImperative(numbers: IntRange) {
    var sum = 0
    for (n in numbers) {
        sum += n
    }
    println(sum)
}

fun scan() {
    runBlocking {
        val reduced: Int = (1..100).reduce { sum, n -> sum + n }
        println("reduced: $reduced")

        val scanned: List<Int> = (1..100).scan(0) { sum, n -> sum + n }
        println("scanned: $scanned")
    }
}
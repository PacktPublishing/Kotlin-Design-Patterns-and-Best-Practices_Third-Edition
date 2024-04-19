import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main() {
    flatMapConcat()
    flatMapMerge()
    flatMapLatest()
    distinctUntilChanged()
    combine()
}

fun combine() {
    runBlocking(Dispatchers.Default) {
        val flowA = ('A'..'Z').map { it.toString() }.asFlow()
        val flowB = ('a'..'z').map { it.toString() }.asFlow()
        val flowC = (1..100).map { it.toString() }.asFlow()
        val flowAB = combine(flowA, flowB) { a, b -> a + b }
        combine(flowAB, flowC) { a, b -> println(a + b) }.collect()
    }
}

fun distinctUntilChanged() {
    runBlocking {
        flowOf(1, 1, 2, 2, 2, 3, 4, 4, 3, 5)
            .distinctUntilChanged()
            .onEach { println(it) }.collect()
    }
}


fun flatMapConcat() {
    runBlocking(Dispatchers.Default) {
        println("flatMapConcat")
        val flowA = ('A'..'B').map { it.toString() }.asFlow()
        val flowB = ('a'..'b').map { it.toString() }.asFlow()
        flowA.flatMapConcat { i -> flowB.map { j -> i + j } }
            .onEach { println(it) }.collect()
    }
}

fun flatMapMerge() {
    runBlocking(Dispatchers.Default) {
        println("flatMapMerge")

        val flowA = ('A'..'Z').map { it.toString() }.asFlow()
        val flowB = ('a'..'z').map { it.toString() }.asFlow()
        flowA.flatMapMerge { i ->
            flowB.map { j -> i + j }
        }.onEach { println(it) }.collect()
    }
}

fun flatMapLatest() {
    runBlocking(Dispatchers.Default) {
        val userSearchInputFlow = flow {
            delay(100)
            emit("K")
            delay(50)
            emit("Ko")
            delay(150)
            emit("Kot")
            delay(450)
        }

        userSearchInputFlow.flatMapConcat { searchTerm ->
            searchApi(searchTerm)
        }.onEach { println(it) }.collect()
    }
}

/**
 * Let's imagine that the output of this function is a flow of search results for the searchTerm
 */
suspend fun searchApi(searchTerm: String): Flow<String> {
    val db = listOf(
        "en.wikipedia.org/wiki/K",
        "www.merriam-webster.com/dictionary/KO",
        "dictionary.cambridge.org/dictionary/english/ko",
        "kotlinlang.org"
    )
    delay(500)
    return db.filter { searchTerm.lowercase() in it.lowercase() }.asFlow()
}

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.fx.coroutines.parMap
import arrow.fx.coroutines.parMapOrAccumulate
import arrow.fx.coroutines.parMapUnordered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.net.URL

suspend fun main() {

    val tasks = 'a'..'z'

    val wikiArticles = tasks.parMap {
        fetchAsync("https://en.wikipedia.org/wiki/$it")
    }

    println(wikiArticles[1]) // ...B, or b, is the second letter...

    val wikiArticlesUnordered = tasks.asFlow().parMapUnordered(concurrency = 2) {
        fetchAsync("https://en.wikipedia.org/wiki/$it")
    }.toList()

    println(wikiArticlesUnordered[1]) // Probably not B

    val wikiArticleOrTimeout: Either<NonEmptyList<RuntimeException>, List<String>> =
        tasks.parMapOrAccumulate { letter ->
            withTimeout(10L) {
                fetchAsync("https://en.wikipedia.org/wiki/$letter")
            }
        }
}

suspend fun fetchAsync(urlToFetch: String): String = withContext(Dispatchers.IO) {
    val url = URL(urlToFetch)
    val inputStream = url.openStream()

    inputStream.bufferedReader().use {
        val content = it.readText()
        content
    }
}

import java.util.*

fun main() {
    val lowerCaseName = JamesBondMovie().run {
        actorName = "ROGER MOORE"
        movieName = "THE MAN WITH THE GOLDEN GUN"
        actorName.lowercase(Locale.getDefault()) // <= Not JamesBond type
    }

    println(lowerCaseName)
}
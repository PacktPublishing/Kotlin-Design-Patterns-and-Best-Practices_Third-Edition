fun main() {
    val bestSeanConneryMovie = JamesBondMovie().apply {
        movieName = "From Ukraine with Love"
    }

    println("${bestSeanConneryMovie.movieName}: ${bestSeanConneryMovie.actorName}")
}

data class JamesBondMovie(
    var actorName: String = "Sean Connery",
    var movieName: String = "From Russia with Love"
)
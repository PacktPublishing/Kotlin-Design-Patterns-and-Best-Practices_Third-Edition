fun main() {
    val myFavoriteMovies = listOf("Black Hawk Down", "Blade Runner")


    val myMovies = NoMoviesList
    val yourMovies = NoMoviesList

    println(myMovies === yourMovies)

    println("Are they same? ${emptyList<String>() === listOf<String>()}")

    println("object vs data object: $NoMoviesList vs $NoMoviesListDataObject")
    Logger.log("Hello World")
}


data object NoMoviesListDataObject
object NoMoviesList : List<String> {

    private val empty = emptyList<String>()
    override val size: Int
        get() = empty.size

    override fun get(index: Int) = empty[index]

    override fun isEmpty() = empty.isEmpty()

    override fun iterator() = empty.iterator()

    override fun listIterator() = empty.listIterator()

    override fun listIterator(index: Int) = empty.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int) = empty.subList(fromIndex, toIndex)

    override fun lastIndexOf(element: String) = empty.lastIndexOf(element)

    override fun indexOf(element: String) = empty.indexOf(element)

    override fun containsAll(elements: Collection<String>) = empty.containsAll(elements)

    override fun contains(element: String) = empty.contains(element)

}

/**
 * Object keyword will create a Singleton
 */
object Logger {
    init {
        println("I was accessed for the first time")

        // Initialization logic goes here
    }

    fun log(message: String) {
        println("Logging $message")
    }
    // More code goes here
}


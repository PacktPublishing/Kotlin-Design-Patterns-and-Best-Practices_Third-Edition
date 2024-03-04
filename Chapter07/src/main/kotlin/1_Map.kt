fun main() {

    val letters = 'a'..'z'
    val ascii = mutableListOf<Int>()

    for (l in letters) {
        ascii.add(l.code)
    }

    println(ascii)
    val result: List<Int> = ('a'..'z').map { it.code }
    println(result)
}

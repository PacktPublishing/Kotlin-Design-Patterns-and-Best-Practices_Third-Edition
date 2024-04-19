fun main() {
    try {
        printNameLength(UserProfile(null, null))
    } catch (e: IllegalArgumentException) {
        println(e)
    }
    printNameLength(UserProfile("Kim", null))
}

fun printNameLength(p: UserProfile) {
    requireNotNull(p.firstName) { "First name must not be null" }
    requireNotNull(p.lastName)
    println(p.firstName.length + 1 + p.lastName.length)
}

class HttpClient {
    var body: String? = null
    var url: String = ""

    fun postRequest() {
        check(body != null) {
            "Body must be set in POST requests"
        }
    }
}
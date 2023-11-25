fun main() {
    withoutBuilderExample()
    builderExample()
    fluentExample()
    applyExample()
    defaultsExample()
}

fun defaultsExample() {
    val mail = MailDefaults(
        title = "Hello",
        message = "There",
        important = true,
        to = listOf("my@dear.cat")
    )
}

fun applyExample() {
    val mail = MailApply(listOf("manager@company.com")).apply {
        message = "You’ve been promoted"
        title = "Come to my office"
    }
}

fun fluentExample() {
    val mail = MailFluent(
        listOf("manager@company.com")
    ).message("Ping")
}

fun builderExample() {
    val mail = MailBuilder()
        .recepients(listOf("hello@world.com"))
        .message("Hello")
        .build()

    println(mail.to)
    println(mail.cc)
    println(mail.message)
}

fun withoutBuilderExample() {
    val mail = RegularMail(
        listOf("manager@company.com"), // To
        null,                      // CC
        "Ping",                   // Title
        null,                 // Message,
        true                  // Important
    )
}

data class RegularMail(
    val to: List<String>,
    val cc: List<String>?,
    val title: String?,
    val message: String?,
    val important: Boolean,
)

class MailBuilder {
    private var recepients: List<String> = listOf()
    private var cc: List<String> = listOf()
    private var title: String = ""
    private var message: String = ""
    private var important: Boolean = false

    data class Mail internal constructor(
        val to: List<String>,
        val cc: List<String>?,
        val title: String?,
        val message: String?,
        val important: Boolean
    )

    fun build(): Mail {
        if (recepients.isEmpty()) {
            throw RuntimeException("To property is empty")
        }

        return Mail(recepients, cc, title, message, important)
    }

    fun message(message: String = ""): MailBuilder {
        this.message = message
        return this
    }

    fun recepients(recepients: List<String>): MailBuilder {
        this.recepients = recepients
        return this
    }

    // More functions to be implemented here
}

data class MailFluent(
    val to: List<String>,
    private var _message: String? = null,
    private var _cc: List<String>? = null,
    private var _title: String? = null,
    private var _important: Boolean? = null
) {
    fun message(message: String) = apply {
        _message = message
    }

    // Pattern repeats for every other field
    //...
}

data class MailApply(
    val to: List<String>,
    var message: String? = null,
    var cc: List<String>? = null,
    var title: String? = null,
    var important: Boolean? = null
)

data class MailDefaults(
    val to: List<String>,
    val cc: List<String> = listOf(),
    val title: String = "",
    val message: String = "",
    val important: Boolean = false,
) 
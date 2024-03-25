import arrow.core.continuations.either
import kotlinx.coroutines.*
import kotlin.random.Random
import arrow.fx.coroutines.*
import kotlin.system.measureTimeMillis

suspend fun main() {
    val t = measureTimeMillis {
        parZip(
            { Me.getFavoriteCharacter().await() },
            { Taylor.getFavoriteCharacter().await() },
            { Michael.getFavoriteCharacter().await() }
        ) { me, taylor, michael ->
            println("Favorite characters are: $me, $taylor, $michael")
        }
    }
    println("Took ${t}ms")
}

data class FavoriteCharacter(
    val name: String,
    val catchphrase: String,
    val picture: ByteArray = Random.nextBytes(42)
)


object Michael {
    suspend fun getFavoriteCharacter() = coroutineScope {
        async {
            delay(300)
            FavoriteCharacter("Terminator", "Hasta la vista, baby")
        }
    }
}

object Taylor {
    suspend fun getFavoriteCharacter() = coroutineScope {
        async {
            delay(200)
            FavoriteCharacter("Don Vito Corleone", "I'm going to make him an offer he can't refuse")
        }
    }
}

object Me {
    suspend fun getFavoriteCharacter() = coroutineScope {
        async {
            delay(100)
            // I already prepared the answer!
            FavoriteCharacter("Inigo Montoya", "Hello, my name is...")
        }
    }
}
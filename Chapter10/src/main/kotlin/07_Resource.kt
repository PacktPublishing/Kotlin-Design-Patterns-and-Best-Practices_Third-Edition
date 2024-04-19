import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.ResourceScope
import arrow.fx.coroutines.resource
import arrow.fx.coroutines.resourceScope
import java.io.BufferedReader
import java.io.FileReader

suspend fun main() {
    BufferedReader(FileReader("./build.gradle.kts")).use {
        println(it.readLines())
    }

    val wardrobe = Wardrobe()
    wardrobe.use {

    }
    val item = getItemFromWardrobe("some socks")
    println(item)

    println(getItemFromWardrobeResource("some socks"))
}

val wardrobeResource: Resource<Wardrobe> = resource(
    { Wardrobe().also { println("Opened wardrobe!") } }
) { wardrobe, _ -> wardrobe.close() }


suspend fun ResourceScope.openWardrobe(): Wardrobe =
    install({ Wardrobe().also { println("Opened wardrobe!") } })
    { wardrobe, _ -> wardrobe.close() }

suspend fun getItemFromWardrobe(itemName: String) = resourceScope {
    val wardrobe = openWardrobe()
    wardrobe.getItem(itemName)
}

suspend fun getItemFromWardrobeResource(itemName: String) = resourceScope {
    val wardrobe = wardrobeResource.bind()
    wardrobe.getItem(itemName)
}

class Wardrobe : AutoCloseable {
    private val drawers: List<Drawer> = listOf()

    override fun close() {
        drawers.forEach {
            it.close()
        }
        println("Closing the wardrobe")
    }

    fun getItem(itemName: String): String? {
        drawers.forEach {
            it.find(itemName)?.let { item ->
                return item
            }
        }

        return null
    }
}

class Drawer : AutoCloseable {

    private val items = listOf<String>()
    override fun close() {
        println("Closing the drawer")
    }

    fun find(itemName: String): String? {
        return items.find { it == itemName }
    }

}

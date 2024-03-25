fun main() {
    val tree = Node(
        1,
        Empty,
        Node(
            2,
            Node(3)
        )
    )

    println(tree)
    println(tree.sum())

    deeplyRecursive()
}

sealed interface Tree<out T>

data object Empty : Tree<Nothing> {
    override fun toString() = "E"
}

data class Node<T>(
    val value: T,
    val left: Tree<T> = Empty,
    val right: Tree<T> = Empty
) : Tree<T> {
    override fun toString(): String {
        var k = value.toString() + " \n" + "/ \\\n"

        k += "$left   $right"

        return k
    }
}

fun Tree<Int>.sum(): Long = when (this) {
    Empty -> 0
    is Node -> value + left.sum() + right.sum()
}

fun deeplyRecursive() {
    var t = Node(1)
    repeat(1_000_000) {
        t = Node(1, Empty, t)
    }

    println(t.deepSum())
}



fun Tree<Int>.deepSum(): Long {
    return DeepRecursiveFunction<Tree<Int>, Long> { tree ->
        when (tree) {
            Empty -> 0
            is Node -> tree.value +
                    callRecursive(tree.left) +
                    callRecursive(tree.right)
        }
    }(this)
}
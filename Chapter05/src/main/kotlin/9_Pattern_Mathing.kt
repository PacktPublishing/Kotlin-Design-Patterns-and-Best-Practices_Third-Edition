fun main() {
    println(getSound(Cat()))
    println(getSound(Dog()))

    try {
        println(getSound(Crow()))
    }
    catch (e: IllegalStateException) {
        println(e)
    }
}

fun getSound(animal: Animal): String {
    var sound: String? = null;

    if (animal is Cat) {
        sound = animal.purr();
    } else if (animal is Dog) {
        sound = animal.bark();
    }

    checkNotNull(sound)

    return sound;
}

class Cat : Animal {
    fun purr(): String {
        return "Purr-purr";
    }
}

class Dog : Animal {
    fun bark(): String {
        return "Bark-bark";
    }
}

class Crow : Animal {
    fun caw(): String {
        return "Caw! Caw!";
    }
}

interface Animal 
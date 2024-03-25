plugins {
    kotlin("jvm") version "2.0.0-Beta4"
}

group = "me.soshin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}


kotlin {
    jvmToolchain(17)
}
plugins {
    kotlin("jvm") version "1.9.10"
}

group = "me.soshin"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
}

kotlin {
    jvmToolchain(11)
}
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "me.soshin"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(20)
}

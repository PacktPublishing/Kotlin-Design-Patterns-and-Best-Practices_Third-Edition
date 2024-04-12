import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.0-RC1"
    application
}

group = "me.soshin"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

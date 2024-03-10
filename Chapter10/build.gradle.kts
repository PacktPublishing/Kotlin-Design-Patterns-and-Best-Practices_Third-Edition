import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.0-Beta4"
    id("com.google.devtools.ksp") version "2.0.0-Beta4-1.0.19"
    application
}

group = "me.soshin"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.arrow-kt:arrow-core:1.2.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("io.arrow-kt:arrow-fx-coroutines:1.2.3")
    implementation("io.arrow-kt:arrow-resilience-jvm:1.2.3")
    implementation("io.arrow-kt:arrow-fx-stm-jvm:1.2.3")
    implementation("io.arrow-kt:arrow-optics:1.2.3")
    ksp("io.arrow-kt:arrow-optics-ksp-plugin:1.2.3")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }
}

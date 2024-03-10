import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.0-Beta4"
    kotlin("plugin.noarg") version "2.0.0-Beta4"
}

group = "me.soshin"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-Xcontext-receivers"
}

noArg {
    annotation("NeedsNoArgs")
}
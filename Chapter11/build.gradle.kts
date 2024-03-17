plugins {
    kotlin("jvm") version "2.0.0-Beta4"
    kotlin("plugin.serialization") version "2.0.0-Beta4"
    application
    id("io.ktor.plugin") version "2.3.9"
}

group = "me.soshin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val exposedVersion = "0.48.0"
dependencies {
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-cio")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.postgresql:postgresql:42.5.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("io.ktor:ktor-server-tests-jvm")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("ServerKt")
}

kotlin {
    jvmToolchain(17)
}
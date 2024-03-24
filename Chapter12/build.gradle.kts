import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.0-Beta5"
    application
}

group = "me.soshin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val vertxVersion = "4.5.6"
dependencies {
    implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))

    implementation("io.vertx:vertx-web")

    implementation("io.vertx:vertx-lang-kotlin")
    implementation("io.vertx:vertx-lang-kotlin-coroutines")
    implementation("io.vertx:vertx-pg-client")
    implementation("org.postgresql:postgresql:42.5.1")
    implementation("com.ongres.scram:client:2.1")
    testImplementation("io.vertx:vertx-web-client")
    testImplementation("io.vertx:vertx-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
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
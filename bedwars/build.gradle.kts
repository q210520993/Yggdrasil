plugins {
    kotlin("jvm") version "2.0.0"
    id("java")
    id("java-library")
}

group = "com.c1ok.yggdrasil"
version = "1.0-Alpha"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.minestom:minestom:2025.09.13-1.21.8")
    implementation("net.minestom:testing:2025.09.13-1.21.8")
    implementation(project(":api"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
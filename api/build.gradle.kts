plugins {
    kotlin("jvm") version "2.0.0"
    id("java")
    id("java-library")
    id("io.github.goooler.shadow") version ("8.1.2")
}

group = "com.c1ok.yggdrasil"
version = "1.0-Alpha"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.minestom:minestom:2025.09.13-1.21.8")
    implementation("net.minestom:testing:2025.09.13-1.21.8")
}

tasks.test {
    useJUnitPlatform()
}
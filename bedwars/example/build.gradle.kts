plugins {
    kotlin("jvm")
}

group = "com.c1ok.yggdrasil"
version = "1.0-Alpha"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    api("org.tinylog:tinylog-api:2.7.0")
    api("org.tinylog:tinylog-impl:2.7.0")
    api("org.tinylog:slf4j-tinylog:2.7.0")
    api("org.slf4j:slf4j-api:2.0.16")
    implementation("net.minestom:minestom:2025.09.13-1.21.8")
    testImplementation("net.minestom:testing:2025.09.13-1.21.8")
    implementation("org.jline:jline-reader:3.25.0")
    implementation("org.jline:jline-terminal:3.25.0")
    implementation("org.jline:jline-terminal-jna:3.25.0")
    implementation("org.fusesource.jansi:jansi:2.4.1")
    api(project(":bedwars"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("java-library")
    id("io.github.goooler.shadow") version ("8.1.2")
}

group = "com.c1ok.yggdrasil"
version = "1.0-Alpha"

subprojects {
    plugins.apply("io.github.goooler.shadow")
    plugins.apply("java-library")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
        maven("https://www.jitpack.io/")
    }
    tasks {
        withType<Copy> {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.compilerArgs.add("-Xlint:deprecation")
            configureEach {
                options.isFork = true
            }
        }
    }
}

tasks.withType<ShadowJar> {
    mergeServiceFiles()
}
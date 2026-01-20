plugins {
    // Use the remap plugin for obfuscated versions like 1.21.11
    id("net.fabricmc.fabric-loom-remap") version "1.14-SNAPSHOT"
    `java-library`
}

version = "2026.01.08-1.21.11"
group = "net.reflact"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.wispforest.io")
    maven("https://jitpack.io")
    maven("https://maven.wispforest.io/")
}

dependencies {
    implementation(project(":common"))
    include(project(":common"))
    // Minecraft
    minecraft("com.mojang:minecraft:1.21.11")

    // Use Official Mojang Mappings
    mappings("net.fabricmc:yarn:1.21.11+build.3:v2")

    // Fabric Loader
    modImplementation("net.fabricmc:fabric-loader:0.18.4")

    // Gson
    implementation("com.google.code.gson:gson:2.11.0")

    // Fabric API
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.141.1+1.21.11")

    // owo-lib
    modImplementation("io.wispforest:owo-lib:0.13.0-alpha.16+1.21.11")
    include("io.wispforest:owo-lib:0.13.0-alpha.16+1.21.11")
    annotationProcessor("io.wispforest:owo-lib:0.13.0-alpha.16+1.21.11")
}

java {
    toolchain {
        // Java 25 is supported in Loom 1.14+
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

loom {
    enableTransitiveAccessWideners.set(false)
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

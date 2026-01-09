plugins {
    id("fabric-loom") version "1.9-SNAPSHOT"
    `java-library`
}

version = "1.0.0"
group = "net.reflect"

repositories {
    mavenCentral()
}

dependencies {
    // Minecraft and mappings
    minecraft("com.mojang:minecraft:1.21.11") // Update to match Minestom protocol
    mappings("net.fabricmc:yarn:1.21.11+build.1:v2") // Update to match MC version

    // Fabric Loader
    modImplementation("net.fabricmc:fabric-loader:0.16.9")

    // Fabric API
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.115.0+1.21.11")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

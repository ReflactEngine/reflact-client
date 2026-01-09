plugins {
    id("fabric-loom") version "1.14-SNAPSHOT"
    `java-library`
}

version = "1.0.0"
group = "net.reflact"

repositories {
    mavenCentral()
}

dependencies {
    // Minecraft and mappings
    minecraft("com.mojang:minecraft:1.21.11")
    mappings(loom.officialMojangMappings())

    // Fabric Loader
    modImplementation("net.fabricmc:fabric-loader:0.16.9")

    // Fabric API
    modImplementation("net.fabricmc.fabric-api:fabric-api:+")
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

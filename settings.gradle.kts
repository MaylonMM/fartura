pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven { url = uri("https://maven.neoforged.net/releases") }
        maven { url = uri("https://maven.minecraftforge.net") }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.8"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// Cada versão/loader suportado é um "node" em versions/<nome>/.
// Para adicionar uma versão: create(rootProject) { versions("1.20.1-forge" to "1.20.1") }
stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"
    create(rootProject) {
        versions("1.21.1-neoforge" to "1.21.1", "1.21.1-forge" to "1.21.1")
    }
}

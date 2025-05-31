pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()

        maven("https://maven.neoforged.net/releases")
	    maven("https://maven.architectury.dev/")
	    maven("https://maven.fabricmc.net/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

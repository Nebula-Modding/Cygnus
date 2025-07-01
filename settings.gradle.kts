pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
		maven(url = "https://maven.msrandom.net/repository/cloche/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

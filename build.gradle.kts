import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    id("maven-publish")
	id("idea")
	id("org.jetbrains.kotlin.jvm") version "2.1.0"
	id("earth.terrarium.cloche") version "0.10.18"
	id("me.fallenbreath.yamlang") version "1.4.1"
}

fun prop(key: String) = property(key) as String

version = prop("mod_version")
group = prop("mod_group_id")

repositories {
    mavenLocal()

	cloche {
		main()
		mavenNeoforged()
		mavenNeoforged("mojang-meta")
		mavenParchment()
		librariesMinecraft()
	}

	// Lazuli
	exclusiveContent {
		forRepository {
			maven("https://jitpack.io/")
		}
		filter {
			includeModule("com.github.emmathemartian", "lazuli")
		}
	}

	// KFF+Dapper
	maven("https://thedarkcolour.github.io/KotlinForForge/")
	maven("https://codeberg.org/EmmaTheMartian/dapper/raw/branch/main/repo/")

	// EMI
	maven("https://maven.terraformersmc.com/")

	// Mekanism
	maven("https://modmaven.dev/")
}

base {
    archivesName = prop("mod_id")
}

java {
	toolchain.languageVersion = JavaLanguageVersion.of(21)
	withSourcesJar()
	withJavadocJar()
}

tasks.named("sourcesJar") {
	dependsOn("generateNeoforgeModsToml")
}

kotlin.compilerOptions.jvmTarget = JvmTarget.JVM_21

cloche {

	minecraftVersion = "1.21.1" //prop("minecraft_version")
	metadata {
		modId = prop("mod_id")
		name = prop("mod_name")
		description = prop("mod_description")
		license = prop("mod_license")
		author(prop("mod_authors"))
	}

	mappings {
		official()
		parchment(prop("parchment_mappings_version"))
	}

	singleTarget {
		neoforge {
			minecraftVersion = "1.21.1" //prop("minecraft_version")
			loaderVersion = prop("neo_version")

			data()

			metadata {
				dependency {
					modId = "lazuli"
					required = true
					version {
						start = "0.1.0"
					}
				}
			}

			mixins.from("cygnus.mixins.json")

			dependencies {
				// Shorthands for Modrinth maven dependencies
				fun modrinth(id: String) = "maven.modrinth:$id:${prop("${id.replace('-', '_')}_version")}"
				fun enabled(id: String) = prop("enable_$id") == "true"

				modImplementation("com.github.emmathemartian:lazuli:${prop("lazuli_version")}")

				// KotlinForForge (for datagen)
				modImplementation("thedarkcolour:kotlinforforge-neoforge:${prop("kff_version")}") {
					// https://github.com/thedarkcolour/KotlinForForge/issues/103
					exclude(group = "net.neoforged.fancymodloader", module = "loader")
				}

				modImplementation("martian:dapper:${prop("dapper_version")}")

				modCompileOnly("dev.emi:emi-neoforge:${prop("emi_version")}:api")
				modLocalRuntime("dev.emi:emi-neoforge:${prop("emi_version")}")

				if (enabled("mekanism"))
					modLocalRuntime("mekanism:Mekanism:${prop("mekanism_version")}")
			}

			runs {
				server {
					jvmArgs("-XX:+AllowEnhancedClassRedefinition")
				}
				client {
					environment("TEST", "Test")

					env("TEST", "Test")
					jvmArgs("-XX:+AllowEnhancedClassRedefinition")
				}
				data()
			}
		}
	}
}

yamlang {
	targetSourceSets = listOf(sourceSets.main.get())
	inputDir = "assets/${prop("mod_id")}/lang"
}

// Exclude the `data` source set from any jars build
tasks.withType<Jar>().configureEach {
	exclude("top/girlkisser/cygnus/datagen/*")
}

publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}
	repositories {
		maven("file://${project.projectDir}/repo")
	}
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
	module {
		isDownloadSources = true
		isDownloadJavadoc = true
	}
}

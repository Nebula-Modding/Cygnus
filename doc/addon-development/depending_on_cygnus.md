# Depending on Cygnus

## Release Versions

Use Modrinth maven.

TODO: Add instructions for Modrinth maven dependencies here once Cygnus is published.

## Development Versions

> tl;dr: Jitpack

Add this to your buildscript:

```groovy
repositories {
  exclusiveContent {
    forRepository {
      maven {
        url = "https://jitpack.com/"
      }
    }
    filter {
      // Make sure that we don't accidentally use Jitpack for things we don't want to use Jitpack for
      includeGroup("com.github.nebula-modding")
    }
  }
}

dependencies {
  modImplementation("com.github.nebula-modding:cygnus:${project.cygnus_version}")
}
```

In your `gradle.properties`:

```properties
# Use the latest Cygnus commit short hash for the version you want to target.
# You can also use main-SNAPSHOT for latest, but you may get a broken build if a bad commit is pushed.
cygnus_version = 425938e
```

And that's it. Simple enough :P

Note that the first build of a given commit will take a *while*. You can check
<https://jitpack.io/#Nebula-Modding/Cygnus> to get build statuses.

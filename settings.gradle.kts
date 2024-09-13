rootProject.name = "nigulpyggub"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/Maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}

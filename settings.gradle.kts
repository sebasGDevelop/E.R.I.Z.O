rootProject.name = "hedgehog-firewall"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

include("domain")
include("application")
include("infrastructure")
include("client")
include("api")
include("match-validator")

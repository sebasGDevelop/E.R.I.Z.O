plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Exposes the generated type-safe accessors for the version catalog
    // to precompiled script plugins in this buildSrc project.
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

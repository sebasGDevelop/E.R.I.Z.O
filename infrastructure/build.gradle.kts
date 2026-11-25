plugins {
    id("firewall.java-conventions")
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))
    implementation(libs.jackson.databind)
}

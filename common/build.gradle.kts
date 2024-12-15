plugins {
    id("java")
    id("io.freefair.lombok") version "8.11"
}

group = "dev.frilly"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.formdev:flatlaf:3.5.2")
    implementation("com.formdev:flatlaf-intellij-themes:3.5.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
}

tasks.jar {
    layout.buildDirectory.set(file("../build/common"))
    destinationDirectory.set(file("../build/common/libs"))
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.test {
    useJUnitPlatform()
}
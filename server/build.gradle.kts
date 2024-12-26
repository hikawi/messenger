plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.freefair.lombok") version "8.11"
    application
}

group = "dev.frilly"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(project(":common"))
    implementation("com.formdev:flatlaf:3.5.2")
    implementation("com.formdev:flatlaf-intellij-themes:3.5.2")
}

application {
    mainClass.set("dev.frilly.messenger.server.Entrypoint")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    layout.buildDirectory.set(file("../build/server"))
    destinationDirectory.set(file("../build/server/libs"))
    manifest {
        attributes("Main-Class" to "dev.frilly.messenger.server.Entrypoint")
    }
}

tasks.shadowJar {
    archiveBaseName.set("Messenger")
    archiveClassifier.set("server")
    archiveVersion.set(version.toString())
}

tasks.test {
    useJUnitPlatform()
}

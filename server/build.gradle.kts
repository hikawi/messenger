plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
//    id("org.springframework.boot") version "3.4.0"
//    id("io.spring.dependency-management") version "1.1.6"
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
//    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-websocket")
//    implementation("org.springframework.boot:spring-boot-starter-validation")
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-jdbc")
//    implementation("org.springframework.boot:spring-boot-starter-security")
//    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
//    implementation("org.postgresql:postgresql:42.7.4")
//
//    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
//    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
//    runtimeOnly("org.springframework.boot:spring-boot-starter-tomcat")
//    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.shadowJar {
    minimize()
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
//
//springBoot {
//    mainClass.set("dev.frilly.messenger.server.Entrypoint")
//}

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

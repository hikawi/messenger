plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
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
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    manifest {
        attributes["Manifest-Version"] = "1.0"
        attributes["Main-Class"] = "dev.frilly.messenger.Application"
    }
    archiveBaseName.set("Messenger")
    archiveVersion.set(version.toString())
    archiveClassifier.set("")
}

tasks.shadowJar {
    archiveBaseName.set("Messenger") // 出力JARの名前
    archiveVersion.set(version.toString()) // バージョン
    archiveClassifier.set("full") // ファイル名に「all」を追加
}

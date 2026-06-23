plugins {
    id("org.springframework.boot") version "4.0.7"
    id("io.spring.dependency-management") version "1.1.7"
    application
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.session.jdbc)

    implementation(libs.json)
    implementation(libs.gson)
    implementation(libs.commons.net)

    implementation(libs.flyway.core)
    implementation(libs.flyway.postgresql)

    runtimeOnly(libs.postgresql)

    testImplementation(libs.spring.boot.starter.test)
}

group = "com.jamii"
version = "0.0.1-SNAPSHOT"
description = "JamiiX"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.jar {
    enabled = false
}

application {
    mainClass.set("com.jamii.ApplicationStart")
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    archiveBaseName.set("app")
    archiveVersion.set("0.0.1")
    mainClass.set("com.jamii.ApplicationStart")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.test {
    failOnNoDiscoveredTests = false
    enabled = false
}

tasks.javadoc {
    options {
        (this as StandardJavadocDocletOptions)
            .addBooleanOption("Xdoclint:all", true)
    }
}
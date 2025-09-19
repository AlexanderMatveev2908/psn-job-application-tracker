plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    java
}

repositories {
    mavenCentral()
}

dependencies {
    // ? side stuff
    implementation(libs.guava)
    implementation(libs.dotenv)

    // ? Spring Boot
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)

    // ? DB driver for app runtime
    runtimeOnly(libs.postgresql)

    // ? Flyway app runtime
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgres)

    // ? dev only
    developmentOnly(libs.spring.boot.devtools)

    // ? tests
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveBaseName.set("server")
    archiveVersion.set("1.0.0")
}

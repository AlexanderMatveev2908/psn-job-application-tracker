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

    // ? spring DB
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.data.r2dbc)
    
    // ? DB driver
    runtimeOnly(libs.postgresql)
    
    // ? async DB
    implementation(libs.r2dbc.postgres)

    // ? migrations
    implementation(libs.liquibase.core)

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

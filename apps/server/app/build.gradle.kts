plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.guava)

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web) 
    implementation(libs.spring.boot.starter.data.jpa) 
    runtimeOnly(libs.postgresql)
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgres)
    implementation(libs.dotenv)
    developmentOnly(libs.spring.boot.devtools)
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


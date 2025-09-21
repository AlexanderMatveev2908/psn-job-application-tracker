import org.gradle.api.plugins.quality.Checkstyle
import com.github.spotbugs.snom.SpotBugsTask
import org.springframework.boot.gradle.tasks.bundling.BootJar


plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    java

    id("checkstyle")
    alias(libs.plugins.spotbugs)
    id("pmd")  
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

    // ? checkers
    compileOnly(libs.spotbugs.annotations)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

checkstyle {
    toolVersion = libs.versions.checkstyle.get()
    config = resources.text.fromFile("config/checkstyle/checkstyle.xml")
}

spotbugs {
    toolVersion.set(libs.versions.spotbugs.asProvider())
}

pmd {
    toolVersion = "7.0.0"
    ruleSets = listOf()
    ruleSetFiles = files("$projectDir/config/pmd/pmd-rules.xml")
    isIgnoreFailures = false
}

tasks.withType<Checkstyle> {
    reports {
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.file("reports/checkstyle/${name}.html"))
    }
}

tasks.withType<Pmd> {
    reports {
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.file("reports/pmd/${name}.html"))
    }
}

tasks.withType<SpotBugsTask> {
    reports.create("html") {
        required.set(true)
        outputLocation.set(layout.buildDirectory.file("reports/spotbugs/${name}.html"))
    }
}

tasks.named("check") {
    dependsOn(
        "checkstyleMain",
        "pmdMain",
        "spotbugsMain"
    )
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<BootJar>("bootJar") {
    archiveBaseName.set("server")
    archiveVersion.set("1.0.0")
}

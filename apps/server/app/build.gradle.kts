import org.gradle.api.plugins.quality.Checkstyle
import com.github.spotbugs.snom.SpotBugsTask
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import com.adarshr.gradle.testlogger.theme.ThemeType



plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    java

    id("checkstyle")
    alias(libs.plugins.spotbugs)
    id("pmd")
    alias(libs.plugins.testlogger)
}

repositories {
    mavenCentral()
       maven { url = uri("https://jitpack.io") }
}

// ! Python CLI depends on these markers //_s  //_e
// ! changing or deleting them will break dependency parsing.

//_s
dependencies {
// ? general
    implementation(libs.guava)
    implementation(libs.dotenv)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    implementation(libs.spring.boot.starter.validation)
    // ? core
    implementation(libs.reactor.core)
    implementation(libs.spring.boot.starter.webflux)
    // ? DB
    implementation(libs.spring.boot.starter.data.r2dbc)
    runtimeOnly(libs.postgresql)
    implementation(libs.r2dbc.postgres)
    // ? migrations
    implementation(libs.liquibase.core)
    // ? redis
    implementation(libs.redis.lettuce)
    // ? mails
    implementation(libs.spring.boot.starter.mail)
    // ? hash
    implementation(libs.argon2)
    // ? checkers
    compileOnly(libs.spotbugsAnnotations)
    // ? tests
    testImplementation(libs.spring.boot.starter.test) {
    exclude(group = "org.junit.vintage")
    }
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    testImplementation(libs.datafaker)
    implementation(libs.javaJwt)
    implementation(libs.nimbusJoseJwt)
    testImplementation(libs.spotbugsAnnotations)
    implementation(libs.jacksonDatatypeJdk8)
    implementation(libs.bcprovJdk18on)
    implementation(libs.datafaker)
    implementation(libs.commonsCodec)
    implementation(libs.javaOtp)
    implementation(libs.core)
    implementation(libs.javase)
}
//_e

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
    toolVersion = libs.versions.pmd.get()
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

    // testLogging {
    //     events("passed", "skipped","failed")
    //     exceptionFormat = TestExceptionFormat.SHORT
    //     showExceptions = false
    //     showCauses = false
    //     showStackTraces = false
    //     showStandardStreams = false
    // }

    // ? silence CDS warnings
    jvmArgs("-Xshare:off")
    // ? silence byte-buddy agent warnings
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}

testlogger {
    theme = ThemeType.MOCHA
    showPassed = false   
    showSkipped = false
    showFailed = true    
    showExceptions = true
    showCauses = true
    showStandardStreams = true
    showStackTraces = false
}

tasks.named<BootJar>("bootJar") {
    archiveBaseName.set("server")
    archiveVersion.set("1.0.0")
}

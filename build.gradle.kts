plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
}

group = "gg.regression"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.3")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("fr.speekha.httpmocker:mocker-okhttp:2.0.0-alpha")
    testImplementation("org.slf4j:slf4j-simple:1.7.36")
    testImplementation("io.kotest:kotest-runner-junit5:5.3.0")
    testImplementation("io.mockk:mockk:1.12.4")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

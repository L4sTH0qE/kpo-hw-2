group = "kpo.homework.two"
version = "1.0-SNAPSHOT"

plugins {
    java
    kotlin("jvm") version "1.9.0"
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass = "kpo.homework.two.utils.Main"
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.13.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
    implementation(kotlin("stdlib-jdk8"))
}
java {
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(15)
}
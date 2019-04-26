buildscript {
    repositories {
        mavenCentral()
    }
}

repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.3.30"
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.30"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.30"
    id("org.springframework.boot") version "2.1.4.RELEASE"
    // id("com.moowork.node") version ("1.3.1")
}

apply(plugin = "io.spring.dependency-management")

group = "dev.yurii.vk"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project("schema"))

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.7")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
//	implementation  ("org.springframework.data:spring-data-rest-hal-browser")
    implementation("org.springframework.session:spring-session-core")

    runtimeOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    implementation(group = "javax.inject", name = "javax.inject", version = "1")
    implementation("com.vk.api:sdk:0.5.11")
}
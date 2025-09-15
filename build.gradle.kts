val ktor_version: String by project
val kotlin_version: String by project

plugins {
    kotlin("jvm") version "2.2.10"
    kotlin("plugin.serialization") version "2.2.20"
    id("org.jetbrains.dokka") version "2.0.0"
    id("org.jetbrains.kotlinx.atomicfu") version "0.29.0"
    id("com.gradleup.nmcp").version("0.0.9")
    jacoco
    signing
    `maven-publish`
}

group = "com.cotiq.lab"
version = "0.0.3"

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-client-cio-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-auth:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")

    implementation("com.nimbusds:nimbus-jose-jwt:10.4.2")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.81")

    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-client-mock:$ktor_version")
}

kotlin {
    jvmToolchain(21)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                val projectGitUrl = "https://github.com/cotiq/coinbase-client-kotlin"

                name = "Unofficial Coinbase Advanced Trade APi client"
                description = "Unofficial SDK for integrating with the Coinbase Advanced Trade API, supporting both REST and WebSocket communication."
                url = projectGitUrl
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "cotiq"
                        name = "Cotiq Lab"
                        email = "lab@cotiq.com"
                    }
                }
                issueManagement {
                    system = "GitHub"
                    url = "$projectGitUrl/issues"
                }
                scm {
                    connection = "scm:git:$projectGitUrl"
                    developerConnection = "scm:git:$projectGitUrl"
                    url = projectGitUrl
                }
            }
        }
    }
}

signing {
    isRequired = !version.toString().contains("SNAPSHOT") && gradle.startParameter.taskNames.contains("publish")
    sign(publishing.publications["mavenJava"])
}

nmcp {
    publish("mavenJava") {
        val mavenCentralUsername: String? by project
        val mavenCentralPassword: String? by project
        username = mavenCentralUsername ?: System.getenv("MAVEN_USERNAME")
        password = mavenCentralPassword ?: System.getenv("MAVEN_PASSWORD")
        publicationType = "AUTOMATIC"
    }
}

jacoco {
    toolVersion = "0.8.9"
    reportsDirectory = layout.buildDirectory.dir("jacoco")
}

tasks.named<Jar>("javadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

tasks {
    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }

    jacocoTestReport {
        dependsOn(test)

        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}

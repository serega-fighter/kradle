object Catalog {

    private object BuildVersions {
        const val kotlin = "1.5.31"
        const val kotest = "4.6.3"
    }

    object Versions {
        const val jvm = "17"
        const val kotlin = "1.6.0"
        const val jmh = "1.34"
        const val jvmKill = "1.16.0"
        const val detekt = "1.19.0"
        const val ktlint = "0.43.2"
        const val kotlinCoroutines = "1.6.0"
        const val kotest = "5.0.3"
        const val mockk = "1.12.1"
        const val junit = "5.8.2"
        const val jacoco = "0.8.7"
    }

    object Plugins {
        val kotlinJvm = Plugin("org.jetbrains.kotlin.jvm", BuildVersions.kotlin)
        val gradlePublish = Plugin("com.gradle.plugin-publish", "0.18.0")
        val testLogger = Plugin("com.adarshr.test-logger", "3.1.0")
    }

    object Dependencies {

        object Platform {
            const val kotlin = "org.jetbrains.kotlin:kotlin-bom:${BuildVersions.kotlin}"
        }

        const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${BuildVersions.kotlin}"
        const val jgit = "org.eclipse.jgit:org.eclipse.jgit:6.0.0.202111291000-r"

        object Plugins {
            const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
            const val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:1.5.31"
            const val allOpen = "org.jetbrains.kotlin:kotlin-allopen:${BuildVersions.kotlin}"
            const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${BuildVersions.kotlin}"
            const val kotlinBenchmark = "org.jetbrains.kotlinx:kotlinx-benchmark-plugin:0.4.0"
            const val testLogger = "com.adarshr:gradle-test-logger-plugin:3.1.0"
            const val shadow = "gradle.plugin.com.github.jengelman.gradle.plugins:shadow:7.0.0"
            const val jib = "gradle.plugin.com.google.cloud.tools:jib-gradle-plugin:3.1.4"
            const val versions = "com.github.ben-manes:gradle-versions-plugin:0.39.0"
            const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.19.0"
            const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:10.2.0"
            const val owaspDependencyCheck = "org.owasp:dependency-check-gradle:6.5.1"
        }

        object Test {
            const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${BuildVersions.kotlin}"
            const val kotestJunit5 = "io.kotest:kotest-runner-junit5:${BuildVersions.kotest}"
            const val kotestProperty = "io.kotest:kotest-property:${BuildVersions.kotest}"
            const val dockerJava = "com.github.docker-java:docker-java:3.2.12"

            val kotestBundle = setOf(kotestJunit5, kotestProperty)
        }
    }

    data class Plugin(val id: String, val version: String)
}

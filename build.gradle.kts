import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0" apply false
    java
}

group = "com.ytree"

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("java")
    }

    java.sourceCompatibilityJvm17()
        .targetCompatibilityJvm17()

    repositories {
        maven(Artifactory.JX_NEXUS)
        mavenCentral()
    }

    val implementation by configurations
    val testImplementation by configurations
    val testRuntimeOnly by configurations

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    }

    tasks {
        kotlinCompile()
            .useJvm17()
        test()
            .useJUnitPlatform()
            .enableLogging()
    }
}

fun JavaPluginExtension.sourceCompatibilityJvm17(): JavaPluginExtension = apply { sourceCompatibility = Java.version }
fun JavaPluginExtension.targetCompatibilityJvm17(): JavaPluginExtension = apply { targetCompatibility = Java.version }

fun TaskContainerScope.kotlinCompile(): TaskCollection<KotlinCompile> = withType(KotlinCompile::class)
fun TaskContainerScope.test(): TaskCollection<Test> = withType(Test::class)

fun TaskCollection<KotlinCompile>.useJvm17(): TaskCollection<KotlinCompile> = apply {
    configureEach { kotlinOptions.jvmTarget = Java.version.toString() }
}
fun TaskCollection<Test>.useJUnitPlatform(): TaskCollection<Test> = apply { configureEach { useJUnitPlatform() } }
fun TaskCollection<Test>.enableLogging(
    vararg logEvents: TestLogEvent = arrayOf(PASSED, STANDARD_OUT, FAILED, STANDARD_ERROR)
): TaskCollection<Test> = apply {
    configureEach {
        with(testLogging) {
            events = logEvents.toSet()
            showExceptions = true
            showStackTraces = true
            showCauses = true
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}

object Java {
    val version: JavaVersion = JavaVersion.VERSION_17
}

object Artifactory {
    const val JX_NEXUS = "https://nexus-jx.jx3.y-tree.uk/repository/maven-group"
}
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  id("com.vanniktech.maven.publish") version versions.mavenPublish
  id("org.jetbrains.dokka") version "1.6.0"
}

val GROUP: String by project
val VERSION_NAME: String by project

group = GROUP
version = VERSION_NAME

tasks.named<Jar>("jar") {
  manifest {
    attributes("Automatic-Module-Name" to "io.github.sullis.kotlin.compiler.util")
  }
}

dependencies {
  api(deps.kotlin.stdlib)
  implementation(deps.kotlin.reflect)
  implementation(deps.kotlin.compiler)
  testImplementation(deps.kotlin.junit)
  testImplementation(deps.kotlin.compiler)
  testImplementation(deps.test.guava)
}

repositories {
  mavenCentral()
}

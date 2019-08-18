import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  // id("com.vanniktech.maven.publish") version versions.mavenPublish
}

val GROUP: String by project
val VERSION_NAME: String by project

group = GROUP
version = VERSION_NAME

tasks.named<Jar>("jar") {
  manifest {
    attributes("Automatic-Module-Name" to "io.github.sullis.kotline.compiler.util")
  }
}

dependencies {
  api(deps.kotlin.stdlib)
  implementation(deps.kotlin.reflect)
  implementation(deps.kotlin.compiler)
  implementation(deps.test.truth)
  testImplementation(deps.kotlin.junit)
  testImplementation(deps.kotlin.compiler)
  testImplementation(deps.test.truth)
}

repositories {
  mavenCentral()
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
  freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
}

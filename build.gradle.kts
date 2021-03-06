plugins {
  kotlin("jvm") version versions.kotlin apply false
  id("com.diffplug.gradle.spotless") version versions.spotless
}

spotless {
  kotlin {
    target("**/*.kt")
    ktlint(versions.ktlint).userData(mapOf("indent_size" to "2"))
    trimTrailingWhitespace()
    endWithNewline()
  }
}

subprojects {
  repositories {
    mavenCentral()
  }
}

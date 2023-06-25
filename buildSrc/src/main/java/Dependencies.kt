object versions {
  const val kotlin = "1.8.22"
  const val spotless = "3.25.0"
  const val ktlint = "0.31.0"
  const val mavenPublish = "0.14.2"
  const val shadowPlugin = "5.0.0"
}

object deps {
  object kotlin {
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val reflect = "org.jetbrains.kotlin:kotlin-reflect"
    const val compiler = "org.jetbrains.kotlin:kotlin-compiler"
    const val junit = "org.jetbrains.kotlin:kotlin-test-junit"
  }
  object test {
    const val guava = "com.google.guava:guava:28.1-jre"
  }
}

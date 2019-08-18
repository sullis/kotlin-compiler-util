object versions {
  const val kotlin = "1.3.41"
  const val spotless = "3.22.0"
  const val ktlint = "0.31.0"
  const val mavenPublish = "0.8.0"
  const val shadowPlugin = "5.0.0"
  const val dokka = "0.9.18"
}

object deps {
  object kotlin {
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7"
    const val reflect = "org.jetbrains.kotlin:kotlin-reflect"
    const val junit = "org.jetbrains.kotlin:kotlin-test-junit"
    const val metadata = "org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.1.0"
  }
  object test {
    const val truth = "com.google.truth:truth:1.0"
  }
}

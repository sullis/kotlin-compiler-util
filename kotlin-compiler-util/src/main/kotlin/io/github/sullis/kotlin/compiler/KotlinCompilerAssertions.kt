package io.github.sullis.kotlin.compiler

import com.google.common.truth.Truth.assertThat

object KotlinCompilerAssertions {

  fun assertKotlinCodeCompiles(sourceDir: java.io.File) {
    assertThat(KotlinCompiler.compile(sourceDir.toPath()).hasErrors()).isFalse()
  }
}

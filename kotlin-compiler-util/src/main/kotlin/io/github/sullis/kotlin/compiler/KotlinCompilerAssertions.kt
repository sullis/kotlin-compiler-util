package io.github.sullis.kotlin.compiler

import com.google.common.truth.Truth.assertThat

object KotlinCompilerAssertions {
  fun assertKotlinCodeCompiles(sourceDir: java.io.File) {
    val result = KotlinCompiler.compile(sourceDir.toPath())
    assertThat(result.isSuccess()).isTrue()
  }
}

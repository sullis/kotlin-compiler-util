package io.github.sullis.kotlin.compiler

import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class KotlinCompilerTest {
    private val goodCode1 = "data class Foo(val a: String, val b: Int)"
    private val goodCode2 = "object Hello { fun echo(name: String): String { return name  } }"

    @Test fun compileHappyPath() {
      val result = KotlinCompiler.compileSourceCode(goodCode1, goodCode2)
      assertTrue(result.isSuccess())
    }

    @Test fun detectBadCode() {
        val result = KotlinCompiler.compileSourceCode("import kotlin.collection.BogusList")
        assertFalse(result.isSuccess())
    }
}
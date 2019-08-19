package io.github.sullis.kotlin.compiler

import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class KotlinCompilerTest {
    private val goodCode1 = "data class Foo(val a: String, val b: Int)"
    private val goodCode2 = "object Hello { fun echo(name: String): String { return name  } }"

    private val badCode = arrayOf(
            "import kotlin.collections.BogusList",
            "{",
            "fun echo()"
    )

    @Test fun compileHappyPath() {
      val result = KotlinCompiler.compileSourceCode(goodCode1, goodCode2)
      assertTrue(result.isSuccess())
    }

    @Test fun badCodeShouldNotCompile() {
        for (code in badCode) {
            assertFalse(KotlinCompiler.compileSourceCode(code).isSuccess())
        }
    }
}

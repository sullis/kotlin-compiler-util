package io.github.sullis.kotlin.compiler

import org.jetbrains.kotlin.cli.common.ExitCode
import org.junit.Assert.*
import org.junit.Test

class KotlinCompilerTest {
    private val goodCode1 = "data class Foo(val a: String, val b: Int)"
    private val goodCode2 = "object Hello { fun echo(name: String): String { return name  } }"
    private val goodCode3 = "import com.google.common.collect.ImmutableList\n\n" +
      "data class House(val rooms: ImmutableList<Room>)\n\n" +
      "data class Room(val name: String)"

    private val badCode = arrayOf(
            "import kotlin.collections.BogusList",
            "{",
            "fun echo()"
    )

    @Test fun compileHappyPath() {
      val result = KotlinCompiler.compileSourceCode(
              goodCode1,
              goodCode2,
              goodCode3)
      assertTrue(result.isSuccess())
      assertEquals(ExitCode.OK, result.exitCode)
      assertEquals(0, result.errors.size)
    }

    @Test fun badCodeShouldNotCompile() {
        for (code in badCode) {
            val result = KotlinCompiler.compileSourceCode(code)
            assertFalse(result.isSuccess())
            assertEquals(ExitCode.COMPILATION_ERROR, result.exitCode)
            assertTrue(result.errors.isNotEmpty())
        }
    }
}

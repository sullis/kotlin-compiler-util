package io.github.sullis.kotlin.compiler

import org.jetbrains.kotlin.cli.common.ExitCode
import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.io.FileWriter
import java.util.*

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
      val result = KotlinCompiler().compileSourceCode(
              goodCode1,
              goodCode2,
              goodCode3)
      assertTrue(result.isSuccess())
      assertEquals(ExitCode.OK, result.exitCode)
      assertEquals(0, result.errors.size)
    }

    @Test fun compileSourceDir() {
        val sourceDir = writeToTempDir(goodCode1, goodCode2, goodCode3)
        val result = KotlinCompiler().compileSourceDir(sourceDir.toPath())
        assertTrue(result.isSuccess())
    }

    @Test fun badClasspath() {
        val cp = Classpath("/dev/null")
        val result = KotlinCompiler(cp).compileSourceCode(goodCode1)
        assertFalse(result.isSuccess())
        assertEquals(ExitCode.INTERNAL_ERROR, result.exitCode)
        assertEquals(1, result.errors.size)
    }

    @Test fun badCodeShouldNotCompile() {
        for (code in badCode) {
            val result = KotlinCompiler().compileSourceCode(code)
            assertFalse(result.isSuccess())
            assertEquals(ExitCode.COMPILATION_ERROR, result.exitCode)
            assertTrue(result.errors.isNotEmpty())
        }
    }

    private fun createTempDir(): File {
      return java.nio.file.Files.createTempDirectory("KotlinCompilerTest").toFile()
    }

    private fun writeToTempDir(vararg things: String): File {
      val tempDir = createTempDir()
      for (thing in things) {
          val f = File(tempDir, UUID.randomUUID().toString() + ".kt")
          val writer = FileWriter(f)
          writer.write(thing)
          writer.flush()
          writer.close()
      }
      return tempDir
    }
}

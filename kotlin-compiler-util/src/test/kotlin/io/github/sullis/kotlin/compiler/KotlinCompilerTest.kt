package io.github.sullis.kotlin.compiler

import org.jetbrains.kotlin.cli.common.ExitCode
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.io.FileWriter
import java.util.UUID

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

    @Test fun defaultConstructorUsesSystemClasspath() {
        val compiler = KotlinCompiler()
        assertEquals(System.getProperty("java.class.path"), compiler.classpath.value)
    }

    @Test fun compileSingleFile() {
        val result = KotlinCompiler().compileSourceCode(goodCode1)
        assertTrue(result.isSuccess())
    }

    @Test fun multipleSequentialCompilationsAreIndependent() {
        val compiler = KotlinCompiler()
        val result1 = compiler.compileSourceCode(goodCode1)
        val result2 = compiler.compileSourceCode(goodCode2)
        val result3 = compiler.compileSourceCode(goodCode3)
        assertTrue(result1.isSuccess())
        assertTrue(result2.isSuccess())
        assertTrue(result3.isSuccess())
    }

    @Test fun errorMessagesHaveErrorSeverity() {
        val result = KotlinCompiler().compileSourceCode("val x: String = 42")
        assertFalse(result.isSuccess())
        assertEquals(ExitCode.COMPILATION_ERROR, result.exitCode)
        assertTrue(result.errors.isNotEmpty())
        for (error in result.errors) {
            assertTrue("Expected error severity, got ${error.severity}", error.severity.isError)
        }
    }

    @Test fun errorMessagesContainText() {
        val result = KotlinCompiler().compileSourceCode("val x: String = 42")
        assertTrue(result.errors.isNotEmpty())
        for (error in result.errors) {
            assertTrue(error.message.isNotBlank())
        }
    }

    @Test fun compileSourceDirWithEmptyDirectory() {
        val emptyDir = java.nio.file.Files.createTempDirectory("KotlinCompilerTest-empty").toFile()
        emptyDir.deleteOnExit()
        val result = KotlinCompiler().compileSourceDir(emptyDir.toPath())
        // Compiling an empty directory produces a result (no crash); the compiler reports no sources found
        assertFalse(result.isSuccess())
    }

    @Test fun compileResultExitCodeMatchesSuccess() {
        val success = KotlinCompiler().compileSourceCode(goodCode1)
        assertEquals(ExitCode.OK, success.exitCode)
        assertEquals(0, success.errors.size)

        val failure = KotlinCompiler().compileSourceCode(badCode[0])
        assertEquals(ExitCode.COMPILATION_ERROR, failure.exitCode)
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

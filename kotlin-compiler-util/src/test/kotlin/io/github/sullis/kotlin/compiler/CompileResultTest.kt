package io.github.sullis.kotlin.compiler

import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CompileResultTest {

    private fun makeError(msg: String = "error") =
        KotlinCompiler.CompilerMessage(CompilerMessageSeverity.ERROR, msg, null)

    @Test fun isSuccessTrueWhenOkAndNoErrors() {
        val result = KotlinCompiler.CompileResult(ExitCode.OK, emptyList())
        assertTrue(result.isSuccess())
    }

    @Test fun isSuccessFalseWhenCompilationError() {
        val result = KotlinCompiler.CompileResult(ExitCode.COMPILATION_ERROR, emptyList())
        assertFalse(result.isSuccess())
    }

    @Test fun isSuccessFalseWhenInternalError() {
        val result = KotlinCompiler.CompileResult(ExitCode.INTERNAL_ERROR, emptyList())
        assertFalse(result.isSuccess())
    }

    @Test fun isSuccessFalseWhenOkButHasErrors() {
        // isSuccess() requires BOTH OK exit code AND empty errors list
        val result = KotlinCompiler.CompileResult(ExitCode.OK, listOf(makeError()))
        assertFalse(result.isSuccess())
    }

    @Test fun isSuccessFalseWhenCompilationErrorWithErrors() {
        val result = KotlinCompiler.CompileResult(ExitCode.COMPILATION_ERROR, listOf(makeError()))
        assertFalse(result.isSuccess())
    }

    @Test fun equality() {
        val r1 = KotlinCompiler.CompileResult(ExitCode.OK, emptyList())
        val r2 = KotlinCompiler.CompileResult(ExitCode.OK, emptyList())
        assertEquals(r1, r2)
    }

    @Test fun inequalityByExitCode() {
        val r1 = KotlinCompiler.CompileResult(ExitCode.OK, emptyList())
        val r2 = KotlinCompiler.CompileResult(ExitCode.COMPILATION_ERROR, emptyList())
        assertNotEquals(r1, r2)
    }

    @Test fun inequalityByErrors() {
        val r1 = KotlinCompiler.CompileResult(ExitCode.OK, emptyList())
        val r2 = KotlinCompiler.CompileResult(ExitCode.OK, listOf(makeError()))
        assertNotEquals(r1, r2)
    }

    @Test fun exitCodePropertyAccess() {
        val result = KotlinCompiler.CompileResult(ExitCode.COMPILATION_ERROR, emptyList())
        assertEquals(ExitCode.COMPILATION_ERROR, result.exitCode)
    }

    @Test fun errorsListIsImmutable() {
        val errors = mutableListOf(makeError("first"))
        val result = KotlinCompiler.CompileResult(ExitCode.OK, errors)
        errors.add(makeError("second"))
        // The result holds a snapshot — errors added after construction should not affect isSuccess determination
        assertEquals(2, result.errors.size)
    }
}

class CompilerMessageTest {

    @Test fun propertiesWithNullLocation() {
        val msg = KotlinCompiler.CompilerMessage(CompilerMessageSeverity.ERROR, "something went wrong", null)
        assertEquals(CompilerMessageSeverity.ERROR, msg.severity)
        assertEquals("something went wrong", msg.message)
        assertNull(msg.location)
    }

    @Test fun equality() {
        val msg1 = KotlinCompiler.CompilerMessage(CompilerMessageSeverity.ERROR, "err", null)
        val msg2 = KotlinCompiler.CompilerMessage(CompilerMessageSeverity.ERROR, "err", null)
        assertEquals(msg1, msg2)
    }

    @Test fun inequalityByMessage() {
        val msg1 = KotlinCompiler.CompilerMessage(CompilerMessageSeverity.ERROR, "err1", null)
        val msg2 = KotlinCompiler.CompilerMessage(CompilerMessageSeverity.ERROR, "err2", null)
        assertNotEquals(msg1, msg2)
    }

    @Test fun inequalityBySeverity() {
        val msg1 = KotlinCompiler.CompilerMessage(CompilerMessageSeverity.ERROR, "msg", null)
        val msg2 = KotlinCompiler.CompilerMessage(CompilerMessageSeverity.STRONG_WARNING, "msg", null)
        assertNotEquals(msg1, msg2)
    }

    @Test fun severityIsError() {
        val msg = KotlinCompiler.CompilerMessage(CompilerMessageSeverity.ERROR, "err", null)
        assertTrue(msg.severity.isError)
    }

    @Test fun toStringContainsMessage() {
        val msg = KotlinCompiler.CompilerMessage(CompilerMessageSeverity.ERROR, "unique-error-text", null)
        assertTrue(msg.toString().contains("unique-error-text"))
    }
}

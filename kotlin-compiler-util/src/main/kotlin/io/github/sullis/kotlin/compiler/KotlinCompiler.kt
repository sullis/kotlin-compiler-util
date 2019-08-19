package io.github.sullis.kotlin.compiler

import java.nio.file.Path
import java.util.ArrayList
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import java.io.FileWriter

object KotlinCompiler {

    fun compileSourceCode(vararg code: String): CompileResult {
        val sourceDir = java.nio.file.Files.createTempDirectory("KotlinCompiler-source").toFile()
        sourceDir.mkdirs()
        sourceDir.deleteOnExit()
        for (i in 0 until code.size) {
            val filename = "$i.kt"
            val file = java.io.File(sourceDir, filename)
            val writer = FileWriter(file, false)
            writer.write(code[i])
            writer.flush()
            writer.close()
        }
        return compileSourceDir(sourceDir.toPath())
    }

    fun compileSourceDir(kotlinSourceDirectory: Path): CompileResult {
        val compilerOutputDir = java.nio.file.Files.createTempDirectory("KotlinCompiler-output").toFile()
        compilerOutputDir.mkdirs()
        compilerOutputDir.deleteOnExit()

        val freeArgList = ArrayList<String>()
        freeArgList.add(kotlinSourceDirectory.toString())
        val msgCollector = MessageCollectorImpl()
        val compiler = K2JVMCompiler()
        val args = K2JVMCompilerArguments()
        args.freeArgs = freeArgList
        args.classpath = System.getProperty("java.class.path")
        args.compileJava = true
        args.noStdlib = true
        args.noReflect = true
        args.destination = compilerOutputDir.getAbsolutePath()
        val exitCode = compiler.exec(msgCollector, Services.EMPTY, args)
        return CompileResult(exitCode, msgCollector.toList())
    }

    data class CompileResult(val exitCode: ExitCode, val errors: List<CompilerMessage>) {
        fun isSuccess(): Boolean = (exitCode == ExitCode.OK) && errors.isEmpty()
    }

    data class CompilerMessage(
            val severity: CompilerMessageSeverity,
            val message: String,
            val location: CompilerMessageLocation?
    )

    private class MessageCollectorImpl : MessageCollector {
        private val errors: MutableList<CompilerMessage> = mutableListOf()
        override fun hasErrors(): Boolean { return (errors.size > 0) }
        override fun clear() { errors.clear() }
        override fun report(severity: CompilerMessageSeverity, message: String, location: CompilerMessageLocation?) {
            if (severity.isError) {
                errors.add(CompilerMessage(severity, message, location))
            }
        }

        override fun toString(): String {
            return errors.toString()
        }

        fun toList(): List<CompilerMessage> = errors.toList()
    }
}

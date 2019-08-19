package io.github.sullis.kotlin.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class JavaCompatibilityTest {
    private KotlinCompiler compiler = new KotlinCompiler();

    @Test
    public void javaSanityCheck() {
        KotlinCompiler.CompileResult result = compiler.compileSourceCode("data class Foo(val a: String)");
        assertTrue(result.isSuccess());
    }
}

package io.github.sullis.kotlin.compiler

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ClasspathTest {

    @Test fun equality() {
        val cp1 = Classpath("a:b:c")
        val cp2 = Classpath("a:b:c")
        val cp3 = Classpath("x:y:z")
        assertEquals(cp1, cp2)
        assertNotEquals(cp1, cp3)
    }

    @Test fun hashCodeConsistency() {
        val cp1 = Classpath("some/path")
        val cp2 = Classpath("some/path")
        assertEquals(cp1.hashCode(), cp2.hashCode())
    }

    @Test fun copyWithNewValue() {
        val original = Classpath("original/path")
        val modified = original.copy(value = "modified/path")
        assertEquals("original/path", original.value)
        assertEquals("modified/path", modified.value)
    }

    @Test fun toStringContainsValue() {
        val cp = Classpath("my-classpath-value")
        assertTrue(cp.toString().contains("my-classpath-value"))
    }

    @Test fun valuePropertyAccess() {
        val path = "/usr/lib/kotlin.jar:/usr/lib/guava.jar"
        val cp = Classpath(path)
        assertEquals(path, cp.value)
    }

    @Test fun componentFunction() {
        val cp = Classpath("test/path")
        val (value) = cp
        assertEquals("test/path", value)
    }
}

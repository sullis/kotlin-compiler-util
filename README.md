# kotlin-compiler-util

Kotlin compiler utility

# Maven pom.xml

```
<dependency>
    <groupId>io.github.sullis</groupId>
    <artifactId>kotlin-compiler-util</artifactId>
    <version>0.0.2</version>
    <scope>test</scope>
</dependency>

```

# Java code example

```

import io.github.sullis.kotlin.compiler.KotlinCompiler;
import io.github.sullis.kotlin.compiler.KotlinCompiler.CompileResult;

KotlinCompiler compiler = new KotlinCompiler();
CompileResult result = compiler.compileSourceDir(sourceDir);
assertTrue(result.isSuccessful(), result.errors());

```

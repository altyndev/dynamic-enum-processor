package io.github.altyndev.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class DynamicEnumProcessorTest {

    @Test
    void shouldGenerateIsMethods() {
        JavaFileObject enumClass = JavaFileObjects.forSourceLines("test.TestEnum",
                "package test;",
                "",
                "import io.github.username.DynamicEnum;",
                "",
                "@DynamicEnum",
                "public enum TestEnum {",
                "    FIRST_VALUE,",
                "    SECOND_VALUE",
                "}"
        );

        Compilation compilation = javac()
                .withProcessors(new DynamicEnumProcessor())
                .compile(enumClass);

        assertThat(compilation).succeeded();
        assertThat(compilation)
                .generatedSourceFile("test.TestEnumMethods")
                .hasSourceEquivalentTo(JavaFileObjects.forSourceLines("test.TestEnumMethods",
                        "package test;",
                        "",
                        "public class TestEnumMethods {",
                        "    public static boolean isFirstValue(TestEnum value) {",
                        "        return value == TestEnum.FIRST_VALUE;",
                        "    }",
                        "",
                        "    public static boolean isSecondValue(TestEnum value) {",
                        "        return value == TestEnum.SECOND_VALUE;",
                        "    }",
                        "}"
                ));
    }
}
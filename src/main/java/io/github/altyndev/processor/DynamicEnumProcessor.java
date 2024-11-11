package io.github.altyndev.processor;

import com.google.auto.service.AutoService;
import io.github.altyndev.DynamicEnum;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes("io.github.altyndev.DynamicEnum")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class DynamicEnumProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(DynamicEnum.class)) {
            if (element.getKind() == ElementKind.ENUM) {
                TypeElement enumElement = (TypeElement) element;
                DynamicEnum annotation = enumElement.getAnnotation(DynamicEnum.class);

                if (annotation.generateIsMethods()) {
                    generateIsMethods(enumElement, annotation.methodPrefix());
                }
            }
        }
        return true;
    }

    private void generateIsMethods(TypeElement enumElement, String methodPrefix) {
        String packageName = processingEnv.getElementUtils()
                .getPackageOf(enumElement).getQualifiedName().toString();
        String enumName = enumElement.getSimpleName().toString();
        String className = enumName + "Methods";

        try {
            JavaFileObject builderFile = processingEnv.getFiler()
                    .createSourceFile(packageName + "." + className);

            try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                out.println("package " + packageName + ";");
                out.println();
                out.println("public class " + className + " {");

                enumElement.getEnclosedElements().stream()
                        .filter(e -> e.getKind() == ElementKind.ENUM_CONSTANT)
                        .forEach(enumConstant -> {
                            String constantName = enumConstant.getSimpleName().toString();
                            String methodName = methodPrefix + toCamelCase(constantName);

                            out.println("    public static boolean " + methodName + "(" +
                                    enumName + " value) {");
                            out.println("        return value == " + enumName + "." +
                                    constantName + ";");
                            out.println("    }");
                            out.println();
                        });

                out.println("}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toCamelCase(String enumConstant) {
        String[] parts = enumConstant.toLowerCase().split("_");
        StringBuilder camelCaseString = new StringBuilder();

        for (String part : parts) {
            camelCaseString.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1));
        }

        return camelCaseString.toString();
    }
}

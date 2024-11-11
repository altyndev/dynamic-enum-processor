package io.github.altyndev;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface DynamicEnum {
    /**
     * Определяет, нужно ли генерировать методы is*.
     * По умолчанию true.
     */
    boolean generateIsMethods() default true;

    /**
     * Определяет префикс для генерируемых методов.
     * По умолчанию "is".
     */
    String methodPrefix() default "is";
}

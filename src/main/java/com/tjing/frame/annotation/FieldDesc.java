package com.tjing.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD })
public @interface FieldDesc {
	String name() default "";
	String comment() default "";
	String value() default "";
	String dic() default "";
}

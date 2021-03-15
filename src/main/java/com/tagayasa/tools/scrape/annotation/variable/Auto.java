package com.tagayasa.tools.scrape.annotation.variable;

import com.tagayasa.tools.scrape.annotation.VariableAnnotation;

import java.lang.annotation.*;

@VariableAnnotation
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface Auto {
	String xpath() default ".";
}

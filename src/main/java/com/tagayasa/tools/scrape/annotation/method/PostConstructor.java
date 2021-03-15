package com.tagayasa.tools.scrape.annotation.method;

import com.tagayasa.tools.scrape.annotation.MethodAnnotation;

import java.lang.annotation.*;

@MethodAnnotation
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface PostConstructor {
}

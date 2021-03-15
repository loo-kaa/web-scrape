package com.tagayasa.tools.scrape.annotation.optional;

import com.tagayasa.tools.scrape.annotation.OptionalAnnotation;

import java.lang.annotation.*;

@OptionalAnnotation
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface WebClientOption {
}

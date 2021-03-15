package com.tagayasa.tools.scrape.annotation.type;

import com.tagayasa.tools.scrape.annotation.TypeAnnotation;

import java.lang.annotation.*;

/**
 * Annotates classes as scraper that will be able to get injected by the framework.
 *
 * This should be the only component annotation.
 */

@TypeAnnotation
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Scraper {
	String xpath() default ".";
}

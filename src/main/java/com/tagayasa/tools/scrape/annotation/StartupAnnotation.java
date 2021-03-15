package com.tagayasa.tools.scrape.annotation;

import java.lang.annotation.*;

/**
 * Marks an annotation as being able to startup a scraping process
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface StartupAnnotation {
}

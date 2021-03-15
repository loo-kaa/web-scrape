package com.tagayasa.tools.scrape.annotation;

import java.lang.annotation.*;

/**
 * Annotates elements that will mark a method as callable by the framework.
 *
 * Only annotations that target methods should be annotated using this.
 * TODO annotations processor that performs this checks
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface MethodAnnotation {
}

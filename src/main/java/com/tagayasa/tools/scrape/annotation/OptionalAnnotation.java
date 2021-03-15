package com.tagayasa.tools.scrape.annotation;

import java.lang.annotation.*;

/**
 * Annotates elements that will have special behaviour and actions associated.
 * An example could be clicking on an element of the page
 *
 * Only annotations that target types, parameters or fields should be annotated using this
 * TODO annotations processor that performs this checks
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface OptionalAnnotation {
}

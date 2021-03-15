package com.tagayasa.tools.scrape.annotation.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

public interface AnnotationData<T extends AnnotatedElement> {
	T getAnnotatedElement();

	boolean hasBaseAnnotation();

	Annotation getBaseAnnotation();

	boolean hasOptionalAnnotations();

	Annotation[] getOptionalAnnotations();
}

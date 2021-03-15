package com.tagayasa.tools.scrape.annotation.parser;

import java.lang.annotation.Annotation;

public class ClassAnnotationData<T> extends BaseAnnotationData<Class<T>> {
	protected ClassAnnotationData(Class<T> annotatedElement) {
		super(annotatedElement);
	}

	protected ClassAnnotationData(Class<T> annotatedElement, Annotation baseAnnotation) {
		super(annotatedElement, baseAnnotation);
	}

	protected ClassAnnotationData(Class<T> annotatedElement, Annotation baseAnnotation, Annotation[] optionalAnnotations) {
		super(annotatedElement, baseAnnotation, optionalAnnotations);
	}
}

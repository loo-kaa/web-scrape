package com.tagayasa.tools.scrape.annotation.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MethodAnnotationData extends BaseAnnotationData<Method> {
	protected MethodAnnotationData(Method annotatedElement) {
		super(annotatedElement);
	}

	protected MethodAnnotationData(Method annotatedElement, Annotation baseAnnotation) {
		super(annotatedElement, baseAnnotation);
	}

	protected MethodAnnotationData(Method annotatedElement, Annotation baseAnnotation, Annotation[] optionalAnnotations) {
		super(annotatedElement, baseAnnotation, optionalAnnotations);
	}
}

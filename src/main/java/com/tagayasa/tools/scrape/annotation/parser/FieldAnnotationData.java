package com.tagayasa.tools.scrape.annotation.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldAnnotationData extends BaseAnnotationData<Field> {
	protected FieldAnnotationData(Field annotatedElement) {
		super(annotatedElement);
	}

	protected FieldAnnotationData(Field annotatedElement, Annotation baseAnnotation) {
		super(annotatedElement, baseAnnotation);
	}

	protected FieldAnnotationData(Field annotatedElement, Annotation baseAnnotation, Annotation[] optionalAnnotations) {
		super(annotatedElement, baseAnnotation, optionalAnnotations);
	}
}

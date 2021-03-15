package com.tagayasa.tools.scrape.annotation.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ParameterAnnotationData extends BaseAnnotationData<Parameter> {
	protected ParameterAnnotationData(Parameter annotatedElement) {
		super(annotatedElement);
	}

	protected ParameterAnnotationData(Parameter annotatedElement, Annotation baseAnnotation) {
		super(annotatedElement, baseAnnotation);
	}

	protected ParameterAnnotationData(Parameter annotatedElement, Annotation baseAnnotation, Annotation[] optionalAnnotations) {
		super(annotatedElement, baseAnnotation, optionalAnnotations);
	}
}

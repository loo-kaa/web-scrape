package com.tagayasa.tools.scrape.annotation.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public abstract class BaseAnnotationData<T extends AnnotatedElement> implements AnnotationData<T> {
	private final T annotatedElement;
	private final Annotation baseAnnotation;
	private final Annotation[] optionalAnnotations;

	protected BaseAnnotationData(T annotatedElement) {
		this.annotatedElement = annotatedElement;

		this.baseAnnotation = null;
		this.optionalAnnotations = null;
	}

	protected BaseAnnotationData(T annotatedElement, Annotation baseAnnotation) {
		this.annotatedElement = annotatedElement;

		this.baseAnnotation = baseAnnotation;
		this.optionalAnnotations = null;
	}

	protected BaseAnnotationData(T annotatedElement, Annotation baseAnnotation, Annotation[] optionalAnnotations) {
		this.annotatedElement = annotatedElement;

		this.baseAnnotation = baseAnnotation;
		this.optionalAnnotations = optionalAnnotations != null && optionalAnnotations.length > 0 ? optionalAnnotations : null;
	}

	public static <T> ClassAnnotationData<T> getAnnotationData(Class<T> annotatedElement, Annotation baseAnnotation) {
		return new ClassAnnotationData<>(annotatedElement, baseAnnotation);
	}

	static <T> ClassAnnotationData<T> getAnnotationData(Class<T> annotatedElement, Annotation baseAnnotation, Annotation[] actionAnnotations) {
		return new ClassAnnotationData<>(annotatedElement, baseAnnotation, actionAnnotations);
	}

	static ParameterAnnotationData getAnnotationData(Parameter annotatedElement, Annotation baseAnnotation, Annotation[] actionAnnotations) {
		return new ParameterAnnotationData(annotatedElement, baseAnnotation, actionAnnotations);
	}

	static FieldAnnotationData getAnnotationData(Field annotatedElement, Annotation baseAnnotation, Annotation[] actionAnnotations) {
		return new FieldAnnotationData(annotatedElement, baseAnnotation, actionAnnotations);
	}

	static MethodAnnotationData getAnnotationData(Method annotatedElement, Annotation baseAnnotation, Annotation[] actionAnnotations) {
		return new MethodAnnotationData(annotatedElement, baseAnnotation, actionAnnotations);
	}

	public final boolean isClassAnnotationData() {
		return this instanceof ClassAnnotationData<?>;
	}

	public final ClassAnnotationData<?> getClassAnnotationData() {
		if(this.isClassAnnotationData()) {
			return (ClassAnnotationData<?>) this;
		}

		throw new IllegalStateException("Not an instance of ClassAnnotationData");
	}

	public final boolean isParameterAnnotationData() {
		return this instanceof ParameterAnnotationData;
	}

	public final ParameterAnnotationData getParameterAnnotationData() {
		if(this.isParameterAnnotationData()) {
			return (ParameterAnnotationData) this;
		}

		throw new IllegalStateException("Not an instance of ParameterAnnotationData");
	}

	public final boolean isFieldAnnotationData() {
		return this instanceof FieldAnnotationData;
	}

	public final FieldAnnotationData getFieldAnnotationData() {
		if(this.isFieldAnnotationData()) {
			return (FieldAnnotationData) this;
		}

		throw new IllegalStateException("Not an instance of FieldAnnotationData");
	}

	@Override
	public T getAnnotatedElement() {
		return this.annotatedElement;
	}

	@Override
	public boolean hasBaseAnnotation() {
		return this.baseAnnotation != null;
	}

	@Override
	public Annotation getBaseAnnotation() {
		return baseAnnotation;
	}

	@Override
	public boolean hasOptionalAnnotations() {
		return this.optionalAnnotations != null;
	}

	@Override
	public Annotation[] getOptionalAnnotations() {
		return optionalAnnotations;
	}
}

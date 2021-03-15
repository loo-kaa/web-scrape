package com.tagayasa.tools.scrape.annotation.parser;

public class JobData<C> {
	private final C context;
	private final ClassBuilder<C> classBuilder;
	private final BaseAnnotationData<?> annotationData;

	public JobData(C context, BaseAnnotationData<?> annotationData) {
		this.context = context;
		this.classBuilder = null;
		this.annotationData = annotationData;
	}

	public JobData(C context, BaseAnnotationData<?> annotationData, ClassBuilder<C> classBuilder) {
		this.context = context;
		this.classBuilder = classBuilder;
		this.annotationData = annotationData;
	}

	public C getContext() {
		return context;
	}

	public ClassBuilder<C> getClassBuilder() {
		return classBuilder;
	}

	public BaseAnnotationData<?> getAnnotationData() {
		return this.annotationData;
	}
}

package com.tagayasa.tools.scrape.annotation.parser;

import com.gargoylesoftware.htmlunit.html.DomNode;

import java.lang.annotation.Annotation;

public abstract class ClassDomJob<A extends Annotation> implements DomJob<A> {
	@Override
	public abstract Class<A> annotation();

	protected abstract Object getInstance(ContextSupplier<DomNode, A> context);

	@Override
	public final Object apply(JobData<DomNode> jobData) {
		BaseAnnotationData<?> annotationData = jobData.getAnnotationData();

		if(annotationData.isClassAnnotationData()) {
			ClassAnnotationData<?> classAnnotationData = annotationData.getClassAnnotationData();

			@SuppressWarnings("unchecked")
			ContextSupplier<DomNode, A> contextSupplier = new ContextSupplier<>(jobData.getContext(),
					classAnnotationData.getAnnotatedElement(),
					(A) classAnnotationData.getBaseAnnotation(),
					classAnnotationData.getOptionalAnnotations(),
					jobData.getClassBuilder());

			return this.getInstance(contextSupplier);
		}
		else {
			throw new IllegalStateException("The AnnotationData passed is not a ClassAnnotationData");
		}
	}

	protected static class ContextSupplier<C, A extends Annotation> {
		public final C data;
		public final Class<?> clazz;
		public final A annotation;
		public final Annotation[] optionalAnnotations;
		public final ClassBuilder<C> builder;

		public ContextSupplier(C data, Class<?> clazz, A annotation, Annotation[] optionalAnnotations, ClassBuilder<C> builder) {
			this.data = data;
			this.clazz = clazz;
			this.annotation = annotation;
			this.optionalAnnotations = optionalAnnotations;
			this.builder = builder;
		}
	}
}

package com.tagayasa.tools.scrape.annotation.parser;

import com.gargoylesoftware.htmlunit.html.DomNode;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

public abstract class VariableDomJob<A extends Annotation> implements DomJob<A> {
	@Override
	public abstract Class<A> annotation();

	protected abstract Object getObject(ContextSupplier<DomNode, A> context);

	@Override
	public final Object apply(JobData<DomNode> jobData) {
		if(jobData.getClassBuilder().isParallelClassBuilder()) {
			BaseAnnotationData<?> annotationData = jobData.getAnnotationData();

			if(annotationData.isParameterAnnotationData()) {
				ParameterAnnotationData parameterAnnotationData = annotationData.getParameterAnnotationData();

				@SuppressWarnings("unchecked")
				ContextSupplier<DomNode, A> contextSupplier = new ContextSupplier<>(jobData.getContext(),
						parameterAnnotationData.getAnnotatedElement().getParameterizedType(),
						(A) parameterAnnotationData.getBaseAnnotation(),
						parameterAnnotationData.getOptionalAnnotations(),
						(ParallelClassBuilder<DomNode>) jobData.getClassBuilder());

				return this.getObject(contextSupplier);
			}
			else if(annotationData.isFieldAnnotationData()) {
				FieldAnnotationData parameterAnnotationData = annotationData.getFieldAnnotationData();

				@SuppressWarnings("unchecked")
				ContextSupplier<DomNode, A> contextSupplier = new ContextSupplier<>(jobData.getContext(),
						parameterAnnotationData.getAnnotatedElement().getGenericType(),
						(A) parameterAnnotationData.getBaseAnnotation(),
						parameterAnnotationData.getOptionalAnnotations(),
						(ParallelClassBuilder<DomNode>) jobData.getClassBuilder());

				return this.getObject(contextSupplier);
			}
			else {
				throw new IllegalStateException("The AnnotationData passed is not a ParameterAnnotationData or a FieldAnnotationData");
			}
		}
		else {
			throw new IllegalStateException("The ClassBuilder must be a ParallelClassBuilder");
		}
	}

	protected static class ContextSupplier<C, A extends Annotation> {
		public final C data;
		public final Type type;
		public final A annotation;
		public final Annotation[] optionalAnnotations;
		public final ParallelClassBuilder<C> builder;

		public ContextSupplier(C data, Type type, A annotation, Annotation[] optionalAnnotations, ParallelClassBuilder<C> builder) {
			this.data = data;
			this.type = type;
			this.annotation = annotation;
			this.optionalAnnotations = optionalAnnotations;
			this.builder = builder;
		}
	}

	protected Class<?> getAnyTypeClass(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			return getAnyTypeClass(parameterizedType.getRawType());
		} else if (type instanceof WildcardType) {
			WildcardType wildcardType = (WildcardType) type;
			Class<?> result;

			for (Type bound : wildcardType.getUpperBounds()) {
				result = getAnyTypeClass(bound);

				if (result != null)
					return result;
			}

			for (Type bound : wildcardType.getUpperBounds()) {
				result = getAnyTypeClass(bound);

				if (result != null)
					return result;
			}

			return Object.class;
		} else if (type instanceof GenericArrayType) {
			GenericArrayType genericArrayType = (GenericArrayType) type;

			return getAnyTypeClass(genericArrayType.getGenericComponentType());
		} else if (type instanceof TypeVariable) {
			TypeVariable<?> typeVariable = (TypeVariable<?>) type;
			Class<?> result;

			for (Type bound : typeVariable.getBounds()) {
				result = getAnyTypeClass(bound);

				if (result != null)
					return result;
			}

			return Object.class;
		} else if (type instanceof Class) {
			Class<?> aClass = (Class<?>) type;

			if (aClass.isArray()) {
				Class<?> elementClass = aClass.getComponentType();

				return getAnyTypeClass(elementClass);
			} else {
				return aClass;
			}
		}

		return null;
	}

	protected Class<?> getInnerType(Type type) {
		return getInnerType(type, 0);
	}

	protected Class<?> getInnerType(Type type, int typeIndex) {
		if (type instanceof ParameterizedType) {
			Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();

			if(typeArguments.length >= typeIndex - 1) {
				return getAnyTypeClass(typeArguments[typeIndex]);
			}
		}

		return null;
	}
}

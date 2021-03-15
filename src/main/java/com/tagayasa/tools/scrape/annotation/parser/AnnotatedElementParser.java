package com.tagayasa.tools.scrape.annotation.parser;

import com.tagayasa.tools.scrape.annotation.MethodAnnotation;
import com.tagayasa.tools.scrape.annotation.VariableAnnotation;
import com.tagayasa.tools.scrape.annotation.OptionalAnnotation;
import com.tagayasa.tools.scrape.annotation.TypeAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class AnnotatedElementParser {
	private AnnotatedElementParser() { }

	public static ParameterAnnotationData[] getAnnotationDataArray(Parameter[] annotatedElements) {
		int l = annotatedElements.length;

		ParameterAnnotationData[] result = new ParameterAnnotationData[l];

		for (int i = 0; i < l; ++i) {
			result[i] = (ParameterAnnotationData) retrieveAnnotationData(annotatedElements[i], VariableAnnotation.class);
		}

		return result;
	}

	public static <T> ClassAnnotationData<T> getAnnotationData(Class<T> clazz) {
		return retrieveAnnotationData(clazz, TypeAnnotation.class);
	}

	public static MethodAnnotationData[] getFilteredAnnotationData(Method[] annotatedElements) {
		int l = annotatedElements.length, c = 0;

		MethodAnnotationData[] result = new MethodAnnotationData[l];

		for (Method annotatedElement : annotatedElements) {
			MethodAnnotationData annotationData = retrieveAnnotationData(annotatedElement, MethodAnnotation.class);

			if (annotationData.hasBaseAnnotation()) {
				result[c++] = annotationData;
			}
		}

		return Arrays.copyOf(result, c);
	}

	public static ClassAnnotationData<?>[] getFilteredAnnotationData(Class<?>[] annotatedElements) {
		int l = annotatedElements.length, c = 0;

		ClassAnnotationData<?>[] result = new ClassAnnotationData[l];

		for (Class<?> annotatedElement : annotatedElements) {
			ClassAnnotationData<?> annotationData = retrieveAnnotationData(annotatedElement, TypeAnnotation.class);

			if (annotationData.hasBaseAnnotation()) {
				result[c++] = annotationData;
			}
		}

		return Arrays.copyOf(result, c);
	}

	public static FieldAnnotationData[] getFilteredAnnotationData(Field[] annotatedElements) {
		int l = annotatedElements.length, c = 0;

		FieldAnnotationData[] result = new FieldAnnotationData[l];

		for (Field annotatedElement : annotatedElements) {
			FieldAnnotationData annotationData = retrieveAnnotationData(annotatedElement, VariableAnnotation.class);

			if (annotationData.hasBaseAnnotation()) {
				result[c++] = annotationData;
			}
		}

		return Arrays.copyOf(result, c);
	}

	public static ParameterAnnotationData[] getFilteredAnnotationData(Parameter[] annotatedElements) {
		int l = annotatedElements.length, c = 0;

		ParameterAnnotationData[] result = new ParameterAnnotationData[l];

		for (Parameter annotatedElement : annotatedElements) {
			ParameterAnnotationData annotationData = retrieveAnnotationData(annotatedElement, VariableAnnotation.class);

			if (annotationData.hasBaseAnnotation()) {
				result[c++] = annotationData;
			}
		}

		return Arrays.copyOf(result, c);
	}

	private static MethodAnnotationData retrieveAnnotationData(Method annotatedElement, Class<? extends Annotation> marker) {
		Annotation[] elementAnnotations = annotatedElement.getAnnotations();
		int l = elementAnnotations.length, c = 0;

 		Annotation baseAnnotation = null;
 		Annotation[] optionalAnnotations = new Annotation[l];

		for (Annotation annotation : elementAnnotations) {
			Class<? extends Annotation> annotationClass = annotation.annotationType();

			if ((baseAnnotation = annotationClass.getAnnotation(marker)) != null) {
				baseAnnotation = annotation;
			} else if (annotationClass.isAnnotationPresent(OptionalAnnotation.class)) {
				optionalAnnotations[c++] = annotation;
			}
		}

		return BaseAnnotationData.getAnnotationData(annotatedElement, baseAnnotation, Arrays.copyOf(optionalAnnotations, c));
	}

	private static <T> ClassAnnotationData<T> retrieveAnnotationData(Class<T> annotatedElement, Class<? extends Annotation> marker) {
		Annotation[] elementAnnotations = annotatedElement.getAnnotations();
		int l = elementAnnotations.length, c = 0;

 		Annotation baseAnnotation = null;
 		Annotation[] optionalAnnotations = new Annotation[l];

		for (Annotation annotation : elementAnnotations) {
			Class<? extends Annotation> annotationClass = annotation.annotationType();

			if ((baseAnnotation = annotationClass.getAnnotation(marker)) != null) {
				baseAnnotation = annotation;
			} else if (annotationClass.isAnnotationPresent(OptionalAnnotation.class)) {
				optionalAnnotations[c++] = annotation;
			}
		}

		return BaseAnnotationData.getAnnotationData(annotatedElement, baseAnnotation, Arrays.copyOf(optionalAnnotations, c));
	}

	private static FieldAnnotationData retrieveAnnotationData(Field annotatedElement, Class<? extends Annotation> marker) {
		Annotation[] elementAnnotations = annotatedElement.getAnnotations();
		int l = elementAnnotations.length, c = 0;

 		Annotation baseAnnotation = null;
 		Annotation[] optionalAnnotations = new Annotation[l];

		for (Annotation annotation : elementAnnotations) {
			Class<? extends Annotation> annotationClass = annotation.annotationType();

			if ((baseAnnotation = annotationClass.getAnnotation(marker)) != null) {
				baseAnnotation = annotation;
			} else if (annotationClass.isAnnotationPresent(OptionalAnnotation.class)) {
				optionalAnnotations[c++] = annotation;
			}
		}

		return BaseAnnotationData.getAnnotationData(annotatedElement, baseAnnotation, Arrays.copyOf(optionalAnnotations, c));
	}

	private static ParameterAnnotationData retrieveAnnotationData(Parameter annotatedElement, Class<? extends Annotation> marker) {
		Annotation[] elementAnnotations = annotatedElement.getAnnotations();
		int l = elementAnnotations.length, c = 0;

 		Annotation baseAnnotation = null;
 		Annotation[] optionalAnnotations = new Annotation[l];

		for (Annotation annotation : elementAnnotations) {
			Class<? extends Annotation> annotationClass = annotation.annotationType();

			if ((baseAnnotation = annotationClass.getAnnotation(marker)) != null) {
				baseAnnotation = annotation;
			} else if (annotationClass.isAnnotationPresent(OptionalAnnotation.class)) {
				optionalAnnotations[c++] = annotation;
			}
		}

		return BaseAnnotationData.getAnnotationData(annotatedElement, baseAnnotation, Arrays.copyOf(optionalAnnotations, c));
	}
}

package com.tagayasa.tools.scrape.annotation.parser;

import com.tagayasa.tools.scrape.annotation.method.PostConstructor;
import com.tagayasa.tools.scrape.exception.ClassBuilderException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

public class ClassBuilderImpl<C> implements ClassBuilder<C> {
	private final AnnotationEngine<C> annotationEngine;
	private final ClassValue<ClassDefinition<?>> classesDefinition = new ClassValue<>() {
		@Override
		protected ClassDefinition<?> computeValue(Class<?> type) {
			return getClassDefinition(type);
		}
	};

	public ClassBuilderImpl(AnnotationEngine<C> annotationEngine) {
		this.annotationEngine = annotationEngine;
	}

	private static <T> ClassDefinition<T> getClassDefinition(Class<T> tClass) {
		ClassDefinition<T> definition = new ClassDefinition<>(tClass);
		definition.setConstructorData(findSuitableConstructor(tClass));
		definition.setFieldsData(AnnotatedElementParser.getFilteredAnnotationData(tClass.getDeclaredFields()));

		for(MethodAnnotationData methodAnnotationData : AnnotatedElementParser.getFilteredAnnotationData(tClass.getDeclaredMethods())) {
			// TODO custom jobs even for methods ? The AnnotationEngine and ClassBuilder could have similar logic to execute Jobs ? Maybe yes

			if(PostConstructor.class.equals(methodAnnotationData.getBaseAnnotation().annotationType())) {
				definition.setPostConstructor(methodAnnotationData,
						AnnotatedElementParser.getAnnotationDataArray(methodAnnotationData.getAnnotatedElement().getParameters()));
			}
		}

		return definition;
	}

	private static <T> ConstructorData<T> findSuitableConstructor(Class<T> tClass) {
		@SuppressWarnings("unchecked") final Constructor<T>[] constructors = (Constructor<T>[]) tClass.getConstructors();
		int constructorsLength = constructors.length;

		if (constructorsLength > 0) {
			float maxAffinity = .0f;

			Constructor<T> selectedConstructor = constructors[0];
			ParameterAnnotationData[] selectedConstructorData = AnnotatedElementParser.getAnnotationDataArray(selectedConstructor.getParameters());

			for (int i = 1; i < constructorsLength; ++i) {
				Constructor<T> constructor = constructors[i];
				Parameter[] parameters = constructor.getParameters();
				ParameterAnnotationData[] parametersAnnotationData = AnnotatedElementParser.getAnnotationDataArray(parameters);

				int hasAnnotationCount = 0, parametersLength = parameters.length;

				for (AnnotationData<?> annotationData : parametersAnnotationData) {
					if (annotationData.hasBaseAnnotation()) {
						hasAnnotationCount++;
					}
				}

				if (hasAnnotationCount == parametersLength) {
					return new ConstructorData<>(selectedConstructor, selectedConstructorData);
				} else {
					float affinity = (float) hasAnnotationCount / parametersLength;

					if (affinity > maxAffinity) {
						maxAffinity = affinity;
						selectedConstructor = constructor;
						selectedConstructorData = parametersAnnotationData;
					}
				}
			}

			return new ConstructorData<>(selectedConstructor, selectedConstructorData);
		}

		throw new ClassBuilderException("No suitable constructor found for class: " + tClass);
	}

	public <T> T construct(Class<T> tClass, C context) {
		@SuppressWarnings("unchecked")
		ClassDefinition<T> definition = (ClassDefinition<T>) this.classesDefinition.get(tClass);

		ConstructorData<T> constructorData = definition.getConstructorData();
		Constructor<T> constructor = constructorData.getConstructor();
		final Object[] arguments = this.getActualParameters(constructorData.getParametersData(), context);

		T instance;

		try {
			instance = constructor.newInstance(arguments);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new ClassBuilderException("Can't initialize the class: " + tClass, e);
		}

		FieldAnnotationData[] fieldsData = definition.getFieldsData();

		for (FieldAnnotationData fieldData : fieldsData) {
			Field field = fieldData.getAnnotatedElement();
			field.setAccessible(true);

			try {
				field.set(instance, this.annotationEngine.process(fieldData, context));
			} catch (IllegalAccessException e) {
				throw new ClassBuilderException("Can't set the field '" + field + "' of the class: " + tClass, e);
			}
		}

		if(definition.hasPostConstructor()) {
			MethodAnnotationData methodAnnotationData = definition.getPostConstructor();
			final Object[] postConstructorArguments = this.getActualParameters(definition.getPostConstructorParametersData(), context);

			try {
				methodAnnotationData.getAnnotatedElement().invoke(instance, postConstructorArguments);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new ClassBuilderException("Exception thrown while invoking the postConstructor of class: " + tClass, e);
			}
		}

		return instance;
	}

	private Object[] getActualParameters(ParameterAnnotationData[] parametersData, C context) {
		final int length = parametersData.length;
		final Object[] arguments = new Object[parametersData.length];

		for (int i = 0; i < length; ++i) {
			ParameterAnnotationData parameterData = parametersData[i];

			if (parameterData.hasBaseAnnotation()) {
				arguments[i] = this.annotationEngine.process(parameterData, context);
			} else {
				arguments[i] = null;
			}
		}

		return arguments;
	}

	private static class ConstructorData<T> {
		private final Constructor<T> constructor;
		private final ParameterAnnotationData[] parametersData;

		public ConstructorData(Constructor<T> constructor, ParameterAnnotationData[] parametersData) {
			this.constructor = constructor;
			this.parametersData = parametersData;
		}

		public Constructor<T> getConstructor() {
			return constructor;
		}

		public ParameterAnnotationData[] getParametersData() {
			return parametersData;
		}
	}

	private static class ClassDefinition<T> {
		private final Class<T> type;
		private ConstructorData<T> constructorData;
		private FieldAnnotationData[] fieldsData;
		private MethodAnnotationData postConstructor;
		private ParameterAnnotationData[] postConstructorParametersData;

		public ClassDefinition(Class<T> type) {
			if (type == null)
				throw new IllegalArgumentException("The class can't be null");

			this.type = type;
		}

		public Class<T> getType() {
			return type;
		}

		public ConstructorData<T> getConstructorData() {
			return constructorData;
		}

		public void setConstructorData(ConstructorData<T> constructorData) {
			this.constructorData = constructorData;
		}

		public FieldAnnotationData[] getFieldsData() {
			return fieldsData;
		}

		public void setFieldsData(FieldAnnotationData[] fieldsData) {
			this.fieldsData = fieldsData;
		}

		public boolean hasPostConstructor() {
			return this.postConstructor != null;
		}

		public void setPostConstructor(MethodAnnotationData postConstructor, ParameterAnnotationData[] postConstructorParametersData) {
			if(postConstructor != null && postConstructorParametersData != null) {
				this.postConstructor = postConstructor;
				this.postConstructorParametersData = postConstructorParametersData;
			}
			else {
				throw new IllegalArgumentException("MethodAnnotationData and ParameterAnnotationData[] can't be null");
			}
		}

		public MethodAnnotationData getPostConstructor() {
			return postConstructor;
		}

		public ParameterAnnotationData[] getPostConstructorParametersData() {
			return postConstructorParametersData;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			ClassDefinition<?> that = (ClassDefinition<?>) o;
			return type.equals(that.type);
		}

		@Override
		public int hashCode() {
			return type.hashCode();
		}
	}
}

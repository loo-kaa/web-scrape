package com.tagayasa.tools.scrape.annotation.parser;

import com.tagayasa.tools.scrape.annotation.StartupAnnotation;
import com.tagayasa.tools.scrape.exception.AnnotationEngineException;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnnotationEngine<C> implements ParallelClassBuilder<C> {
	private final Map<Class<? extends Annotation>, Job<C, ?>> annotationsJobs;
	private final Map<Class<?>, ClassAnnotationData<?>> classesAnnotationData;
	private final ClassBuilderImpl<C> classBuilderImpl;
	private final ExecutorService executor;

	public AnnotationEngine(Job<C, ?>[] annotationsJobsArray, Class<?>[] classesDefinitions) {
		this.annotationsJobs = new HashMap<>();
		this.classesAnnotationData = new HashMap<>();

		for (Job<C, ?> job : annotationsJobsArray) {
			annotationsJobs.put(job.annotation(), job);
		}

		for (ClassAnnotationData<?> classAnnotationData : AnnotatedElementParser.getFilteredAnnotationData(classesDefinitions)) {
			this.classesAnnotationData.put(classAnnotationData.getAnnotatedElement(), classAnnotationData);
		}

		this.classBuilderImpl = new ClassBuilderImpl<>(this);
		this.executor = Executors.newWorkStealingPool(1);
	}

	@Override
	public <T> T construct(Class<T> tClass, C context) {
		@SuppressWarnings("unchecked")
		ClassAnnotationData<T> classAnnotationData = (ClassAnnotationData<T>) this.classesAnnotationData.get(tClass);

		if (classAnnotationData != null) {
			return executeClassJob(classAnnotationData, context);
		}

		throw new AnnotationEngineException("No class definition found for type '" + tClass + "' maybe the type needs an Annotation");
	}

	@Override
	public <T> List<T> parallelConstruct(Class<?> tClass, List<C> contexts) {
		@SuppressWarnings("unchecked")
		ClassAnnotationData<T> classAnnotationData = (ClassAnnotationData<T>) this.classesAnnotationData.get(tClass);

		if (classAnnotationData != null) {
			int length = contexts.size();
			@SuppressWarnings("unchecked")
			CompletableFuture<T>[] futures = (CompletableFuture<T>[]) new CompletableFuture[length];

			for (int i = 0; i < length; ++i) {
				C context = contexts.get(i);

				futures[i] = CompletableFuture.supplyAsync(() -> executeClassJob(classAnnotationData, context), this.executor);
			}

			CompletableFuture.allOf(futures).join();
			List<T> resultList = new ArrayList<>(length);

			for (CompletableFuture<T> future : futures) {
				try {
					T result = future.get();

					resultList.add(result);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new AnnotationEngineException("The thread got interrupted while waiting for the CompletableFuture result", e);
				} catch (ExecutionException e) {
					throw new AnnotationEngineException("ExecutionException thrown during CompletableFuture execution", e);
				}
			}

			return resultList;
		}

		throw new AnnotationEngineException("No class definition found for type '" + tClass + "' maybe the type needs an Annotation");
	}

	@SuppressWarnings("unchecked")
	private <A extends Annotation, T> T executeClassJob(ClassAnnotationData<T> classAnnotationData, C context) {
		Job<C, A> job = (Job<C, A>) this.annotationsJobs.get(classAnnotationData.getBaseAnnotation().annotationType());

		if (job != null) {
			// in the case of class jobs we pass directly the classBuilderImpl we don't want to cause loops
			JobData<C> jobData = new JobData<>(context, classAnnotationData, this.classBuilderImpl);

			return (T) job.apply(jobData);
		} else {
			// pass through if there are no jobs
			return this.classBuilderImpl.construct(classAnnotationData.getAnnotatedElement(), context);
		}
	}

	public <T> T startProcess(ClassAnnotationData<T> annotationData, C context) {
		if (annotationData.hasBaseAnnotation()) {
			return this.executeClassJob(annotationData, context);
		} else {
			throw new IllegalArgumentException("The class '" + annotationData.getBaseAnnotation() + "' is not annotated with a relevant annotation");
		}
	}

	public List<?> startProcesses() {
		List<ClassAnnotationData<?>> startupClassesData = new ArrayList<>();

		for (ClassAnnotationData<?> classAnnotationData : this.classesAnnotationData.values()) {
			Class<? extends Annotation> annotationType = classAnnotationData.getBaseAnnotation().annotationType();

			if (annotationType.getAnnotation(StartupAnnotation.class) != null) {
				startupClassesData.add(classAnnotationData);
			}
		}

		int length = startupClassesData.size();
		CompletableFuture<?>[] futures = (CompletableFuture<?>[]) new CompletableFuture[length];

		for (int i = 0; i < length; ++i) {
			ClassAnnotationData<?> startupClassData = startupClassesData.get(i);

			futures[i] = CompletableFuture.supplyAsync(() -> executeClassJob(startupClassData, null), this.executor);
		}

		CompletableFuture.allOf(futures).join();
		List<Object> resultList = new ArrayList<>(length);

		for (CompletableFuture<?> future : futures) {
			try {
				resultList.add(future.get());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new AnnotationEngineException("The thread got interrupted while waiting for the CompletableFuture result", e);
			} catch (ExecutionException e) {
				throw new AnnotationEngineException("ExecutionException thrown during CompletableFuture execution", e);
			}
		}

		return resultList;
	}

	public <A extends Annotation> Object process(ParameterAnnotationData annotationData, C context) {
		@SuppressWarnings("unchecked")
		Job<C, A> job = (Job<C, A>) this.annotationsJobs.get(annotationData.getBaseAnnotation().annotationType());
		JobData<C> jobData = new JobData<>(context, annotationData, this);

		return job.apply(jobData);
	}

	public <A extends Annotation> Object process(FieldAnnotationData annotationData, C context) {
		@SuppressWarnings("unchecked")
		Job<C, A> job = (Job<C, A>) this.annotationsJobs.get(annotationData.getBaseAnnotation().annotationType());
		JobData<C> jobData = new JobData<>(context, annotationData, this);

		return job.apply(jobData);
	}
}

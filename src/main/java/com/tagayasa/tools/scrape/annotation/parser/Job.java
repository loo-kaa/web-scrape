package com.tagayasa.tools.scrape.annotation.parser;

import java.lang.annotation.Annotation;
import java.util.function.Function;

public interface Job<C, A extends Annotation> extends Function<JobData<C>, Object> {
	Class<A> annotation();

	@Override
	Object apply(JobData<C> jobContextData);
}

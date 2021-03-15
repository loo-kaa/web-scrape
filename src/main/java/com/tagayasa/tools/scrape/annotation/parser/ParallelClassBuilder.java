package com.tagayasa.tools.scrape.annotation.parser;

import java.util.List;

public interface ParallelClassBuilder<C> extends ClassBuilder<C> {
	@Override
	<T> T construct(Class<T> tClass, C context);

	@Override
	default boolean isParallelClassBuilder() {
		return true;
	}

	<T> List<T> parallelConstruct(Class<?> tClass, List<C> contexts);
}

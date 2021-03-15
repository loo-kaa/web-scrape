package com.tagayasa.tools.scrape.annotation.parser;

import java.util.List;

public interface ClassBuilder<C> {
	<T> T construct(Class<T> tClass, C context);

	default boolean isParallelClassBuilder() {
		return false;
	}

	/**
	 * Unsupported operation
	 */
	default <T> List<T> parallelConstruct(Class<?> tClass, List<C> contexts) {
		throw new UnsupportedOperationException("The parallelConstruct method is not supported in class: " + this);
	}
}

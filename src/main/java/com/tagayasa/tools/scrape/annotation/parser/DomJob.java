package com.tagayasa.tools.scrape.annotation.parser;

import com.gargoylesoftware.htmlunit.html.DomNode;

import java.lang.annotation.Annotation;

public interface DomJob<A extends Annotation> extends Job<DomNode, A> {
	@Override
	Class<A> annotation();

	@Override
	Object apply(JobData<DomNode> jobData);
}

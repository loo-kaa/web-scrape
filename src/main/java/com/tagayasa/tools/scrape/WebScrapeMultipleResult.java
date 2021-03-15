package com.tagayasa.tools.scrape;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.tagayasa.tools.scrape.annotation.parser.AnnotationEngine;
import com.tagayasa.tools.scrape.classpath.ClasspathScanner;

import java.util.List;

public class WebScrapeMultipleResult implements WebScrape<List<?>> {
	private final List<?> result;

	protected WebScrapeMultipleResult(Package mainPackage) {
		final Class<?>[] packageClasses = ClasspathScanner.getClasses(mainPackage.getName());

		AnnotationEngine<DomNode> domAnnotationEngine = new AnnotationEngine<>(JOBS, packageClasses);

		result = domAnnotationEngine.startProcesses();
	}

	@Override
	public List<?> getResult() {
		return this.result;
	}
}

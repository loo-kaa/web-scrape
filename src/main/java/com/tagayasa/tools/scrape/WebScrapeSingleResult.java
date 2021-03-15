package com.tagayasa.tools.scrape;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.tagayasa.tools.scrape.annotation.parser.AnnotatedElementParser;
import com.tagayasa.tools.scrape.annotation.parser.AnnotationEngine;
import com.tagayasa.tools.scrape.annotation.parser.BaseAnnotationData;
import com.tagayasa.tools.scrape.annotation.type.Scraper;
import com.tagayasa.tools.scrape.annotation.type.UrlScraper;
import com.tagayasa.tools.scrape.classpath.ClasspathScanner;

import java.lang.annotation.Annotation;

public class WebScrapeSingleResult<T> implements WebScrape<T> {
	private final T result;

	protected WebScrapeSingleResult(Class<T> tClass) {
		final Class<?>[] packageClasses = ClasspathScanner.getClasses(tClass.getPackage().getName());

		AnnotationEngine<DomNode> domAnnotationEngine = new AnnotationEngine<>(JOBS, packageClasses);

		result = domAnnotationEngine.startProcess(AnnotatedElementParser.getAnnotationData(tClass), null);
	}

	protected WebScrapeSingleResult(Class<T> tClass, DomNode context) {
		final Class<?>[] packageClasses = ClasspathScanner.getClasses(tClass.getPackage().getName());

		AnnotationEngine<DomNode> domAnnotationEngine = new AnnotationEngine<>(JOBS, packageClasses);

		Scraper constructedScraper = new Scraper() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return Scraper.class;
			}

			@Override
			public String xpath() {
				return ".";
			}
		};

		result = domAnnotationEngine.startProcess(BaseAnnotationData.getAnnotationData(tClass, constructedScraper), context);
	}

	protected WebScrapeSingleResult(Class<T> tClass, String url) {
		final Class<?>[] packageClasses = ClasspathScanner.getClasses(tClass.getPackage().getName());

		AnnotationEngine<DomNode> domAnnotationEngine = new AnnotationEngine<>(JOBS, packageClasses);

		UrlScraper constructedUrlScraper = new UrlScraper() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return UrlScraper.class;
			}

			@Override
			public String url() {
				return url;
			}
		};

		result = domAnnotationEngine.startProcess(BaseAnnotationData.getAnnotationData(tClass, constructedUrlScraper), null);
	}

	public T getResult() {
		return result;
	}
}

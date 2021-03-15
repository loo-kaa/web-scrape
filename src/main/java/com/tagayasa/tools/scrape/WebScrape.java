package com.tagayasa.tools.scrape;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.tagayasa.tools.scrape.annotation.parser.DomJob;
import com.tagayasa.tools.scrape.annotation.parser.job.*;

import java.util.List;

public interface WebScrape<T> {
	DomJob<?>[] JOBS = new DomJob[] {
			new UrlScraperJob(),
			new ElementJob(),
			new ScraperJob(),
			new AutoJob(),
			new TextContentJob()
	};

	/**
	 * Starts the parsing process from a single class already annotated
	 * @param tClass Already annotated class @UrlScraper
	 * @param <T> The type of the class
	 * @return An initialized instance of the class
	 */
	static <T> WebScrape<T> run(Class<T> tClass) {
		return new WebScrapeSingleResult<>(tClass);
	}

	/**
	 * Starts the parsing process from a single class whether it's annotated or not.
	 * The parsing will be started using as context the page retrieved by the passed url.
	 * Other annotation present on this class will be ignored.
	 * form the page at the passed url.
	 * @param tClass Class not annotated
	 * @param url The url of the page to parse
	 * @param <T> The type of the class
	 * @return An initialized instance of the class
	 */
	static <T> WebScrape<T> run(Class<T> tClass, String url) {
		return new WebScrapeSingleResult<>(tClass, url);
	}

	/**
	 * Starts the parsing process from a single class whether it's annotated or not. However the annotation present
	 * on this class will be ignored. The context used for parsing is the one passed as context parameter.
	 * @param tClass Class used as parser
	 * @param context DomNode context the parsing will start from this element
	 * @param <T> The type of the class
	 * @return An initialized instance of the class
	 */
	static <T> WebScrape<T> run(Class<T> tClass, DomNode context) {
		return new WebScrapeSingleResult<>(tClass, context);
	}

	/**
	 * Starts the parsing process from a package. All classes annotated with a Startup annotation will be executed and
	 * the results for every of them will be returned in a list.
	 * @param mainPackage Package that will be scanned
	 * @return An initialized instance of the class
	 */
	static WebScrape<List<?>> run(Package mainPackage) {
		return new WebScrapeMultipleResult(mainPackage);
	}

	T getResult();

/*

	// run with a baseClass which marks the package to be scanned
	public static WebScrape run(Class<?> baseClass) {
		final List<Class<?>> packageClasses = ClasspathScanner.getClasses(baseClass.getPackage().getName());

		return new WebScrape(baseClass, pageURL, packageClasses);
	}*/
}

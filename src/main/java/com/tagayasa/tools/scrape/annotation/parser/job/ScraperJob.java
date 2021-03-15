package com.tagayasa.tools.scrape.annotation.parser.job;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.tagayasa.tools.scrape.annotation.parser.ClassDomJob;
import com.tagayasa.tools.scrape.annotation.type.Scraper;

public class ScraperJob extends ClassDomJob<Scraper> {
	private static final String XPATH_CURRENT_ELEMENT = ".";

	@Override
	public Class<Scraper> annotation() {
		return Scraper.class;
	}

	@Override
	protected Object getInstance(ContextSupplier<DomNode, Scraper> context) {
		String xpath = context.annotation.xpath();

		if(!XPATH_CURRENT_ELEMENT.equals(xpath)) {
			return context.builder.construct(context.clazz, context.data.getFirstByXPath(xpath));
		}
		else {
			return context.builder.construct(context.clazz, context.data);
		}
	}
}

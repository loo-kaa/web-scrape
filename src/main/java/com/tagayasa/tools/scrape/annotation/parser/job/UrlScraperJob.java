package com.tagayasa.tools.scrape.annotation.parser.job;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.tagayasa.tools.scrape.annotation.parser.ClassDomJob;
import com.tagayasa.tools.scrape.annotation.type.UrlScraper;
import com.tagayasa.tools.scrape.exception.ScrapingException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlScraperJob extends ClassDomJob<UrlScraper> {
	@Override
	public Class<UrlScraper> annotation() {
		return UrlScraper.class;
	}

	@Override
	protected Object getInstance(ContextSupplier<DomNode, UrlScraper> context) {
		DomNode node = this.getPage(context.annotation.url());

		return context.builder.construct(context.clazz, node);
	}

	private void setOptions(WebClientOptions options) {
		options.setActiveXNative(false);
		options.setDownloadImages(false);
		options.setWebSocketEnabled(false);
		options.setCssEnabled(false);
		options.setAppletEnabled(false);
		options.setJavaScriptEnabled(true);
		options.setScreenWidth(1280);
		options.setScreenHeight(720);
		options.setRedirectEnabled(true);
	}

	private HtmlPage getPage(String url) {
		try (WebClient client = new WebClient()) {
			setOptions(client.getOptions());

			return client.getPage(new URL(url));
		} catch (MalformedURLException e) {
			throw new ScrapingException("MalformedURLException url exception while processing: " + url, e);
		} catch (IOException e) {
			throw new ScrapingException("IOException while getting the page at url: " + url, e);
		}
	}
}

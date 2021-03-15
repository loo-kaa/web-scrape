package com.tagayasa.tools.scrape.exception;

public class ScrapingException extends RuntimeException {
	public ScrapingException() {
		super();
	}

	public ScrapingException(String message) {
		super(message);
	}

	public ScrapingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ScrapingException(Throwable cause) {
		super(cause);
	}

	protected ScrapingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

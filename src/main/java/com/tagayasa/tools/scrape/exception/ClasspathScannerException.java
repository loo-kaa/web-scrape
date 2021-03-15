package com.tagayasa.tools.scrape.exception;

public class ClasspathScannerException extends RuntimeException {
	public ClasspathScannerException() {
		super();
	}

	public ClasspathScannerException(String message) {
		super(message);
	}

	public ClasspathScannerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClasspathScannerException(Throwable cause) {
		super(cause);
	}

	protected ClasspathScannerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
package com.tagayasa.tools.scrape.exception;

public class AnnotationEngineException extends RuntimeException {
	public AnnotationEngineException() {
		super();
	}

	public AnnotationEngineException(String message) {
		super(message);
	}

	public AnnotationEngineException(String message, Throwable cause) {
		super(message, cause);
	}

	public AnnotationEngineException(Throwable cause) {
		super(cause);
	}

	protected AnnotationEngineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

package com.tagayasa.tools.scrape.classpath;

import com.tagayasa.tools.scrape.exception.ClasspathScannerException;

import java.net.URL;
import java.util.stream.Stream;

public abstract class ProtocolScanner {
	public static char REFERENCE_SEPARATOR = '.';

	private final char separator;

	protected ProtocolScanner(char separator) {
		this.separator = separator;
	}

	private String getPackagePath(String packageName) {
		return packageName.replace(REFERENCE_SEPARATOR, this.separator);
	}

	private String getClassName(String classPath) {
		// TOCHECK here the REFERENCE_SEPARATOR is used as the dot denoting the file extension even if they are note the same
		return classPath.substring(0, classPath.lastIndexOf(REFERENCE_SEPARATOR))
				.replace(this.separator, REFERENCE_SEPARATOR);
	}

	public Stream<String> getClassNames(URL packageURL, String packageName) throws ClasspathScannerException {
		return this.getClassPaths(packageURL, getPackagePath(packageName))
					.map(this::getClassName);
	}

	protected abstract Stream<String> getClassPaths(URL packageURL, String packagePath) throws ClasspathScannerException;
}

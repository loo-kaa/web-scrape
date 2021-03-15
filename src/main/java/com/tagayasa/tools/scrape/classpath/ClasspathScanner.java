package com.tagayasa.tools.scrape.classpath;

import com.tagayasa.tools.scrape.exception.ClasspathScannerException;
import org.tinylog.Logger;

import java.io.File;
import java.net.URL;
import java.util.*;

public class ClasspathScanner {
	private static ProtocolScanner getProtocolScanner(String protocol) {
		protocol = protocol.toLowerCase();

		if(protocol.equals("jar")) {
			return new JarProtocolScanner();
		}
		else if(protocol.equals("file")) {
			return new FileProtocolScanner();
		}

		return null;
	}

	private static String getPackagePath(String packageName) {
		return packageName.replace(ProtocolScanner.REFERENCE_SEPARATOR, File.separatorChar);
	}

	private static URL getPackageURL(String packagePath) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return classLoader.getResource(packagePath);
	}

	public static Class<?>[] getClasses(String packageName) {
		final URL packageURL = getPackageURL(getPackagePath(packageName));

		assert packageURL != null : "The package resource can't be found";

		String packageProtocol = packageURL.getProtocol();
		ProtocolScanner protocolScanner = getProtocolScanner(packageProtocol);

		if (protocolScanner != null) {
			return protocolScanner
					.getClassNames(packageURL, packageName)
					.map(className -> {
						try {
							return Class.forName(className);
						} catch (ClassNotFoundException e) {
							Logger.warn("ClassNotFoundException for class name '{}'", className, e);
						}

						return null;
					})
					.filter(Objects::nonNull)
					.toArray(Class[]::new);
		}
		else {
			throw new ClasspathScannerException("Package protocol not supported: " + packageProtocol);
		}
	}
}

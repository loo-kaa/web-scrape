package com.tagayasa.tools.scrape.classpath;

import com.tagayasa.tools.scrape.exception.ClasspathScannerException;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

public class JarProtocolScanner extends ProtocolScanner {
	/**
	 * If we use the default File.separatorChar we will have problems in Windows,
	 * inside a Jar the separator used for resources is '/' and given that the
	 * default Windows separator is '\' we won't find any resource.
	 * So we will always use the '/' as separator.
	 */
	protected JarProtocolScanner() {
		super('/');
	}

	private Stream<String> getClassNamesFromJar(String jarFileName) throws IOException {
		JarFile jarFile = new JarFile(jarFileName);

		return jarFile.stream()
				.parallel()
				.filter(jarEntry -> !jarEntry.isDirectory())
				.map(ZipEntry::getName);
	}

	private String getJarFileName(String jarFileName) {
		jarFileName = URLDecoder.decode(jarFileName, StandardCharsets.UTF_8);
		return jarFileName.substring(5, jarFileName.indexOf("!"));
	}

	@Override
	protected Stream<String> getClassPaths(URL packageURL, String packagePath) throws ClasspathScannerException {
		try {
			return getClassNamesFromJar(getJarFileName(packageURL.getFile()))
					.filter(filePath -> filePath.startsWith(packagePath));
		} catch (IOException e) {
			throw new ClasspathScannerException("IOException while loading class files from jar", e);
		}
	}
}

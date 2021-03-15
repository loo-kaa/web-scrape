package com.tagayasa.tools.scrape.classpath;

import com.tagayasa.tools.scrape.exception.ClasspathScannerException;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileProtocolScanner extends ProtocolScanner {
	protected FileProtocolScanner() {
		super(File.separatorChar);
	}

	private static Stream<Path> getClassNamesFromDirectory(Path path) throws IOException {
		return Files.list(path)
				.parallel()
				.flatMap(innerPath -> {
					try {
						if (Files.isDirectory(innerPath)) {
							return getClassNamesFromDirectory(innerPath);
						} else {
							return Stream.of(innerPath);
						}
					} catch (IOException e) {
						Logger.warn("Exception while reading the file '{}'", innerPath, e);
					}

					return Stream.empty();
				});
	}

	@Override
	protected Stream<String> getClassPaths(URL packageURL, String packagePath) {
		try {
			final Path packageClassesPath = Path.of(packageURL.toURI());
			final int appPathLength = packageClassesPath.getNameCount() - Path.of(packagePath).getNameCount();

			return getClassNamesFromDirectory(packageClassesPath)
					.map(classPath -> classPath.subpath(appPathLength, classPath.getNameCount()).toString());
		} catch (URISyntaxException e) {
			throw new ClasspathScannerException("URISyntaxException while loading class files", e);
		} catch (IOException e) {
			throw new ClasspathScannerException("IOException while loading class files from file system", e);
		}
	}
}

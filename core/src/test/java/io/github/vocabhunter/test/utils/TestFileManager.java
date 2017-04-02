/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.test.utils;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class TestFileManager {
    private final Path directory;

    public TestFileManager(final Class<?> owner) throws IOException {
        directory = Files.createTempDirectory(owner.getSimpleName());
        directory.toFile().deleteOnExit();
    }

    public Path addCopy(final String name) throws URISyntaxException, IOException {
        URL resource = TestFileManager.class.getResource("/" + name);
        Path original = Paths.get(resource.toURI());
        Path copy = addFile(name);

        Files.copy(original, copy, StandardCopyOption.REPLACE_EXISTING);

        return copy;
    }

    public Path addFile(final String name) {
        Path file = directory.resolve(name);

        file.toFile().deleteOnExit();

        return file;
    }

    public Path getDirectory() {
        return directory;
    }

    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(directory.toFile());
    }
}

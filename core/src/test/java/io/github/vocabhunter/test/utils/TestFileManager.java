/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.test.utils;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestFileManager {
    private final Path directory;

    public TestFileManager(final Class<?> owner) throws IOException {
        directory = Files.createTempDirectory(owner.getSimpleName());
        directory.toFile().deleteOnExit();
    }

    public Path addFile(final String name) {
        Path file = directory.resolve(name);

        file.toFile().deleteOnExit();

        return file;
    }

    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(directory.toFile());
    }
}

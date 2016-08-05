/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class BaseSettingsManager<T> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseSettingsManager.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final List<String> MINIMAL_JSON = Collections.singletonList("{}");

    private final Path settingsFile;

    private final Class<T> beanClass;

    protected BaseSettingsManager(final String filename, final Class<T> beanClass) {
        this(SettingsPathTool.obtainSettingsFilePath(filename), beanClass);
    }

    protected BaseSettingsManager(final Path settingsFile, final Class<T> beanClass) {
        this.settingsFile = settingsFile;
        this.beanClass = beanClass;
        ensureDirectoryExists(settingsFile);
    }

    private void ensureDirectoryExists(final Path settingsFile) {
        Path parent = settingsFile.getParent();

        try {
            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (final IOException e) {
            throw new VocabHunterException(String.format("Unable to create directory %s", parent), e);
        }
    }

    protected T readSettings() {
        try {
            T result = readSettingsIfAvailable();

            if (result == null) {
                Files.write(settingsFile, MINIMAL_JSON);
                result = readSettingsIfAvailable();
            }

            return result;
        } catch (final IOException e) {
            throw new VocabHunterException(String.format("Unable to load settings file '%s'", settingsFile), e);
        }
    }

    private T readSettingsIfAvailable() {
        try {
            if (Files.isRegularFile(settingsFile)) {
                return readSettingsInternal();
            } else {
                return null;
            }
        } catch (Exception e) {
            LOG.error("Unable to load settings file '{}'", settingsFile, e);

            return null;
        }
    }

    private T readSettingsInternal() throws IOException {
        return MAPPER.readValue(settingsFile.toFile(), beanClass);
    }

    protected void writeSettings(final T settings) {
        try {
            MAPPER.writeValue(settingsFile.toFile(), settings);
        } catch (final IOException e) {
            throw new VocabHunterException(String.format("Unable to save settings file '%s'", settingsFile), e);
        }
    }
}

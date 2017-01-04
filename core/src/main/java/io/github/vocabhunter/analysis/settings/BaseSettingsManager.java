/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import io.github.vocabhunter.analysis.core.FileTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public class BaseSettingsManager<T> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseSettingsManager.class);

    private final Path settingsFile;

    private final Class<T> beanClass;

    protected BaseSettingsManager(final String filename, final Class<T> beanClass) {
        this(SettingsPathTool.obtainSettingsFilePath(filename), beanClass);
    }

    protected BaseSettingsManager(final Path settingsFile, final Class<T> beanClass) {
        this.settingsFile = settingsFile;
        this.beanClass = beanClass;
        FileTool.ensureDirectoryExists(settingsFile, "Unable to create directory %s");
    }

    protected T readSettings() {
        T result = readSettingsIfAvailable();

        if (result == null) {
            FileTool.writeMinimalJson(settingsFile, "Unable to save settings file '%s'");
            result = readSettingsIfAvailable();
        }

        return result;
    }

    private T readSettingsIfAvailable() {
        try {
            if (Files.isRegularFile(settingsFile)) {
                return FileTool.readFromJson(beanClass, settingsFile, "Unable to load settings file '%s'");
            } else {
                return null;
            }
        } catch (final Exception e) {
            LOG.error("Discarding unreadable settings file '{}'", settingsFile, e);

            return null;
        }
    }

    protected void writeSettings(final T settings) {
        FileTool.writeAsJson(settingsFile, settings, "Unable to save settings file '%s'");
    }
}

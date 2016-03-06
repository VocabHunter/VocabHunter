/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vocabhunter.analysis.core.VocabHunterException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SettingsManagerImpl implements SettingsManager {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private static final List<String> MINIMAL_JSON = Collections.singletonList("{}");

    private final Path settingsFile;

    public SettingsManagerImpl() {
        this(SettingsPathTool.obtainSettingsFilePath());
    }

    public SettingsManagerImpl(final Path settingsFile) {
        this.settingsFile = settingsFile;
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

    @Override
    public Path getDocumentsPath() {
        return getPath(VocabHunterSettings::getDocumentsPath);
    }

    @Override
    public void setDocumentsPath(final Path path) {
        setPath(VocabHunterSettings::setDocumentsPath, path);
    }

    @Override
    public Path getSessionsPath() {
        return getPath(VocabHunterSettings::getSessionsPath);
    }

    @Override
    public void setSessionsPath(final Path path) {
        setPath(VocabHunterSettings::setSessionsPath, path);
    }

    @Override
    public Path getExportPath() {
        return getPath(VocabHunterSettings::getExportPath);
    }

    @Override
    public void setExportPath(final Path path) {
        setPath(VocabHunterSettings::setExportPath, path);
    }

    @Override
    public int getFilterMinimumLetters() {
        return getInt(VocabHunterSettings::getFilterMinimumLetters);
    }

    @Override
    public void setFilterMinimumLetters(final int count) {
        setInt(VocabHunterSettings::setFilterMinimumLetters, count);
    }

    @Override
    public int getFilterMinimumOccurrences() {
        return getInt(VocabHunterSettings::getFilterMinimumOccurrences);
    }

    @Override
    public void setFilterMinimumOccurrences(final int count) {
        setInt(VocabHunterSettings::setFilterMinimumOccurrences, count);
    }

    private int getInt(final Function<VocabHunterSettings, Integer> getter) {
        VocabHunterSettings settings = readSettings();

        return getter.apply(settings);
    }

    private void setInt(final BiConsumer<VocabHunterSettings, Integer> setter, final int value) {
        VocabHunterSettings settings = readSettings();

        setter.accept(settings, value);
        writeSettings(settings);
    }

    private Path getPath(final Function<VocabHunterSettings, String> getter) {
        VocabHunterSettings settings = readSettings();
        String pathName = getter.apply(settings);

        if (pathName != null) {
            Path path = Paths.get(pathName);

            if (Files.isDirectory(path)) {
                return path;
            }
        }

        return Paths.get(System.getProperty("user.home"));
    }

    private void setPath(final BiConsumer<VocabHunterSettings, String> setter, final Path path) {
        VocabHunterSettings settings = readSettings();

        setter.accept(settings, path.toString());
        writeSettings(settings);
    }

    private VocabHunterSettings readSettings() {
        try {
            if (!Files.isRegularFile(settingsFile)) {
                Files.write(settingsFile, MINIMAL_JSON, UTF_8);
            }
            return MAPPER.readValue(settingsFile.toFile(), VocabHunterSettings.class);
        } catch (final IOException e) {
            throw new VocabHunterException(String.format("Unable to load settings file '%s'", settingsFile), e);
        }
    }

    private void writeSettings(final VocabHunterSettings settings) {
        try {
            MAPPER.writeValue(settingsFile.toFile(), settings);
        } catch (final IOException e) {
            throw new VocabHunterException(String.format("Unable to save settings file '%s'", settingsFile), e);
        }
    }
}

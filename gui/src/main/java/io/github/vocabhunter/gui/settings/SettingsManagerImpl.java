/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.settings;

import io.github.vocabhunter.analysis.settings.BaseSettingsManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.inject.Singleton;

@Singleton
public class SettingsManagerImpl extends BaseSettingsManager<VocabHunterSettings> implements SettingsManager {
    public static final String SETTINGS_JSON = "settings.json";

    public SettingsManagerImpl() {
        super(SETTINGS_JSON, VocabHunterSettings.class);
    }

    public SettingsManagerImpl(final Path settingsFile) {
        super(settingsFile, VocabHunterSettings.class);
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
    public Path getWordListPath() {
        return getPath(VocabHunterSettings::getWordListPath);
    }

    @Override
    public void setWordListPath(final Path path) {
        setPath(VocabHunterSettings::setWordListPath, path);
    }

    @Override
    public int getFilterMinimumLetters() {
        return getValue(VocabHunterSettings::getFilterMinimumLetters);
    }

    @Override
    public void setFilterMinimumLetters(final int count) {
        setValue(VocabHunterSettings::setFilterMinimumLetters, count);
    }

    @Override
    public int getFilterMinimumOccurrences() {
        return getValue(VocabHunterSettings::getFilterMinimumOccurrences);
    }

    @Override
    public void setFilterMinimumOccurrences(final int count) {
        setValue(VocabHunterSettings::setFilterMinimumOccurrences, count);
    }

    @Override
    public boolean isAllowInitialCapitals() {
        return getValue(VocabHunterSettings::isAllowInitialCapitals);
    }

    @Override
    public void setAllowInitialCapitals(final boolean allow) {
        setValue(VocabHunterSettings::setAllowInitialCapitals, allow);
    }

    @Override
    public Optional<WindowSettings> getWindowSettings() {
        WindowSettings value = getValue(VocabHunterSettings::getWindowSettings);

        return Optional.ofNullable(value);
    }

    @Override
    public void setWindowSettings(final WindowSettings windowSettings) {
        setValue(VocabHunterSettings::setWindowSettings, windowSettings);
    }

    private <T> T getValue(final Function<VocabHunterSettings, T> getter) {
        VocabHunterSettings settings = readSettings();

        return getter.apply(settings);
    }

    private <T> void setValue(final BiConsumer<VocabHunterSettings, T> setter, final T value) {
        VocabHunterSettings settings = readSettings();

        setter.accept(settings, value);
        writeSettings(settings);
    }

    private Path getPath(final Function<VocabHunterSettings, Path> getter) {
        VocabHunterSettings settings = readSettings();
        Path path = getter.apply(settings);

        if (path != null && Files.isDirectory(path)) {
            return path;
        }

        return Paths.get(System.getProperty("user.home"));
    }

    private void setPath(final BiConsumer<VocabHunterSettings, Path> setter, final Path path) {
        VocabHunterSettings settings = readSettings();

        setter.accept(settings, path);
        writeSettings(settings);
    }
}

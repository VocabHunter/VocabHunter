/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.nio.file.Path;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VocabHunterSettings {
    public static final int DEFAULT_MINIMUM_LETTERS = 2;

    public static final int DEFAULT_MINIMUM_OCCURRENCES = 2;

    public static final boolean DEFAULT_ALLOW_INITIAL_CAPITALS = true;

    private Path documentsPath;

    private Path sessionsPath;

    private Path exportPath;

    private Path wordListPath;

    private int filterMinimumLetters = DEFAULT_MINIMUM_LETTERS;

    private int filterMinimumOccurrences = DEFAULT_MINIMUM_OCCURRENCES;

    private boolean isAllowInitialCapitals = DEFAULT_ALLOW_INITIAL_CAPITALS;

    private WindowSettings windowSettings;

    public Path getDocumentsPath() {
        return documentsPath;
    }

    public void setDocumentsPath(final Path documentsPath) {
        this.documentsPath = documentsPath;
    }

    public Path getSessionsPath() {
        return sessionsPath;
    }

    public void setSessionsPath(final Path sessionsPath) {
        this.sessionsPath = sessionsPath;
    }

    public Path getExportPath() {
        return exportPath;
    }

    public void setExportPath(final Path exportPath) {
        this.exportPath = exportPath;
    }

    public Path getWordListPath() {
        return wordListPath;
    }

    public void setWordListPath(final Path wordListPath) {
        this.wordListPath = wordListPath;
    }

    public int getFilterMinimumLetters() {
        return filterMinimumLetters;
    }

    public void setFilterMinimumLetters(final int filterMinimumLetters) {
        this.filterMinimumLetters = filterMinimumLetters;
    }

    public int getFilterMinimumOccurrences() {
        return filterMinimumOccurrences;
    }

    public void setFilterMinimumOccurrences(final int filterMinimumOccurrences) {
        this.filterMinimumOccurrences = filterMinimumOccurrences;
    }

    public boolean isAllowInitialCapitals() {
        return isAllowInitialCapitals;
    }

    public void setAllowInitialCapitals(final boolean allowInitialCapitals) {
        isAllowInitialCapitals = allowInitialCapitals;
    }

    public WindowSettings getWindowSettings() {
        return windowSettings;
    }

    public void setWindowSettings(final WindowSettings windowSettings) {
        this.windowSettings = windowSettings;
    }
}

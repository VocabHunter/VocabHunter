/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.settings;

import java.nio.file.Path;
import java.util.Optional;

public interface SettingsManager {
    Path getDocumentsPath();

    void setDocumentsPath(Path path);

    Path getSessionsPath();

    void setSessionsPath(Path path);

    Path getExportPath();

    void setExportPath(Path path);

    Path getWordListPath();

    void setWordListPath(Path path);

    int getFilterMinimumLetters();

    void setFilterMinimumLetters(int count);

    int getFilterMinimumOccurrences();

    void setFilterMinimumOccurrences(int count);

    boolean isAllowInitialCapitals();

    void setAllowInitialCapitals(boolean allow);

    Optional<WindowSettings> getWindowSettings();

    void setWindowSettings(WindowSettings windowSettings);
}

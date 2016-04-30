/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.settings.FileListManager;
import io.github.vocabhunter.analysis.settings.ListedFile;
import io.github.vocabhunter.analysis.settings.ListedFileType;
import io.github.vocabhunter.gui.model.FilterFile;
import io.github.vocabhunter.gui.model.FilterFileMode;
import io.github.vocabhunter.gui.model.FilterSettings;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.settings.SettingsManager;

import java.util.List;
import java.util.stream.Collectors;

import static io.github.vocabhunter.gui.model.FilterFileMode.getMode;

public class FilterHandler {
    private final MainModel model;

    private final SettingsManager settingsManager;

    private final FileListManager fileListManager;

    public FilterHandler(final MainModel model, final SettingsManager settingsManager, final FileListManager fileListManager) {
        this.model = model;
        this.settingsManager = settingsManager;
        this.fileListManager = fileListManager;
    }

    public void prepare() {
        int minimumLetters = settingsManager.getFilterMinimumLetters();
        int minimumOccurrences = settingsManager.getFilterMinimumOccurrences();
        boolean allowInitialCapitals = settingsManager.isAllowInitialCapitals();
        List<FilterFile> filterFiles = getFilterFiles(fileListManager);
        FilterSettings settings = new FilterSettings(minimumLetters, minimumOccurrences, allowInitialCapitals, filterFiles);

        model.setFilterSettings(settings);
        model.filterSettingsProperty().addListener(((o, old, v) -> updateFilterSettings(v)));
    }

    private void updateFilterSettings(final FilterSettings settings) {
        settingsManager.setFilterMinimumLetters(settings.getMinimumLetters());
        settingsManager.setFilterMinimumOccurrences(settings.getMinimumOccurrences());
        settingsManager.setAllowInitialCapitals(settings.isAllowInitialCapitals());
        fileListManager.setFilteredSessionFiles(getListedFiles(settings));
    }

    private List<FilterFile> getFilterFiles(final FileListManager fileListManager) {
        return fileListManager.getFilteredSessionFiles().stream()
                .map(this::getFilterFile)
                .collect(Collectors.toList());
    }

    private FilterFile getFilterFile(final ListedFile file) {
        FilterFileMode mode = getMode(file.isIncludeUnknown());

        return new FilterFile(file.getFile(), mode);
    }

    private List<ListedFile> getListedFiles(final FilterSettings settings) {
        return settings.getFilterFiles().stream()
                .map(f -> new ListedFile(f.getFile(), ListedFileType.SESSION, f.getMode().isIncludeUnknown()))
                .collect(Collectors.toList());
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.settings.BaseListedFile;
import io.github.vocabhunter.analysis.settings.FileListManager;
import io.github.vocabhunter.analysis.settings.SessionListedFile;
import io.github.vocabhunter.gui.model.FilterFile;
import io.github.vocabhunter.gui.model.FilterFileMode;
import io.github.vocabhunter.gui.model.FilterSettings;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.settings.SettingsManager;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

import static io.github.vocabhunter.gui.model.FilterFileMode.getMode;

@Singleton
public class FilterHandler {
    private final MainModel model;

    private final SettingsManager settingsManager;

    private final FileListManager fileListManager;

    @Inject
    public FilterHandler(final MainModel model, final SettingsManager settingsManager, final FileListManager fileListManager) {
        this.model = model;
        this.settingsManager = settingsManager;
        this.fileListManager = fileListManager;
    }

    public void initialise() {
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
        fileListManager.setFilterFiles(getListedFiles(settings));
    }

    private List<FilterFile> getFilterFiles(final FileListManager fileListManager) {
        return fileListManager.getFilterFiles().stream()
                .map(this::getFilterFile)
                .collect(Collectors.toList());
    }

    private FilterFile getFilterFile(final BaseListedFile file) {
        if (file instanceof SessionListedFile) {
            SessionListedFile sessionFile = (SessionListedFile) file;
            FilterFileMode mode = getMode(sessionFile.isIncludeUnknown());

            return new FilterFile(file.getFile(), mode);
        } else {
            throw new VocabHunterException("Unknown file type " + file);
        }
    }

    private List<BaseListedFile> getListedFiles(final FilterSettings settings) {
        return settings.getFilterFiles().stream()
                .map(f -> new SessionListedFile(f.getFile(), f.getMode().isIncludeUnknown()))
                .collect(Collectors.toList());
    }
}

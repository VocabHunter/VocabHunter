/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.settings.BaseListedFile;
import io.github.vocabhunter.analysis.settings.FileListManager;
import io.github.vocabhunter.gui.model.FilterSettings;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.services.FilterService;
import io.github.vocabhunter.gui.settings.SettingsManager;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FilterHandler {
    private final MainModel model;

    private final SettingsManager settingsManager;

    private final FileListManager fileListManager;

    private final FilterService filterService;

    @Inject
    public FilterHandler(final MainModel model, final SettingsManager settingsManager, final FileListManager fileListManager, final FilterService filterService) {
        this.model = model;
        this.settingsManager = settingsManager;
        this.fileListManager = fileListManager;
        this.filterService = filterService;
    }

    public void initialise() {
        int minimumLetters = settingsManager.getFilterMinimumLetters();
        int minimumOccurrences = settingsManager.getFilterMinimumOccurrences();
        boolean allowInitialCapitals = settingsManager.isAllowInitialCapitals();
        List<BaseListedFile> filterFiles = fileListManager.getFilterFiles();
        FilterSettings settings = new FilterSettings(minimumLetters, minimumOccurrences, allowInitialCapitals, filterFiles);

        filterService.setFilterSettings(settings);
        model.filterSettingsProperty().addListener((o, old, v) -> updateFilterSettings(v));
    }

    private void updateFilterSettings(final FilterSettings settings) {
        settingsManager.setFilterMinimumLetters(settings.getMinimumLetters());
        settingsManager.setFilterMinimumOccurrences(settings.getMinimumOccurrences());
        settingsManager.setAllowInitialCapitals(settings.isAllowInitialCapitals());
        fileListManager.setFilterFiles(settings.getFilterFiles());
    }
}

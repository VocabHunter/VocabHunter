/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.model.FilterSettings;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.settings.SettingsManager;

public class FilterHandler {
    private final MainModel model;

    private final SettingsManager settingsManager;

    public FilterHandler(final MainModel model, final SettingsManager settingsManager) {
        this.model = model;
        this.settingsManager = settingsManager;
    }

    public void prepare() {
        int minimumLetters = settingsManager.getFilterMinimumLetters();
        int minimumOccurrences = settingsManager.getFilterMinimumOccurrences();
        boolean allowInitialCapitals = settingsManager.isAllowInitialCapitals();
        FilterSettings settings = new FilterSettings(minimumLetters, minimumOccurrences, allowInitialCapitals);

        model.setFilterSettings(settings);
        model.filterSettingsProperty().addListener(((o, old, v) -> updateFilterSettings(v)));
    }

    private void updateFilterSettings(final FilterSettings settings) {
        settingsManager.setFilterMinimumLetters(settings.getMinimumLetters());
        settingsManager.setFilterMinimumOccurrences(settings.getMinimumOccurrences());
        settingsManager.setAllowInitialCapitals(settings.isAllowInitialCapitals());
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.gui.model.FilterSettings;
import io.github.vocabhunter.gui.model.FilterSettingsTool;
import io.github.vocabhunter.gui.model.MainModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FilterService {
    private static final WordFilter NOP_FILTER = w -> true;

    private final MainModel model;

    private final FilterSettingsTool tool;

    @Inject
    public FilterService(final MainModel model, final FilterSettingsTool tool) {
        this.model = model;
        this.tool = tool;
    }

    public void setFilterSettings(final FilterSettings settings) {
        if (!settings.equals(model.getFilterSettings())) {
            model.setFilter(tool.filter(settings));
            model.setFilterSettings(settings);
        }
    }

    public WordFilter getFilter(final boolean isEnableFilters) {
        if (isEnableFilters) {
            return model.getFilter();
        } else {
            return NOP_FILTER;
        }
    }
}

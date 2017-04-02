/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.settings.BaseListedFile;
import io.github.vocabhunter.test.utils.AbstractBeanTest;

import java.util.Collections;
import java.util.List;

public class FilterSettingsTest extends AbstractBeanTest<FilterSettings> {
    private static final List<BaseListedFile> FILES = Collections.emptyList();

    @Override
    protected FilterSettings buildPrimary() {
        return new FilterSettings(1, 1, false, FILES);
    }

    @Override
    protected FilterSettings buildSecondary() {
        return new FilterSettings(1, 1, true, FILES);
    }
}

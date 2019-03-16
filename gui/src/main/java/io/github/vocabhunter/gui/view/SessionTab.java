/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import io.github.vocabhunter.gui.i18n.I18nKey;

import static io.github.vocabhunter.gui.i18n.I18nKey.SESSION_TAB_ANALYSIS;
import static io.github.vocabhunter.gui.i18n.I18nKey.SESSION_TAB_PROGRESS;

public enum SessionTab {
    ANALYSIS(SESSION_TAB_ANALYSIS, "tabAnalysis"), PROGRESS(SESSION_TAB_PROGRESS, "tabProgress");

    private final I18nKey key;

    private final String id;

    SessionTab(final I18nKey key, final String id) {
        this.key = key;
        this.id = id;
    }

    public I18nKey getKey() {
        return key;
    }

    public String getId() {
        return id;
    }
}

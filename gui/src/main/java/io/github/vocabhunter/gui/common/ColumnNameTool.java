/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ColumnNameTool {
    private static final int ALPHABET_LENGTH = 26;

    private final I18nManager i18nManager;

    @Inject
    public ColumnNameTool(final I18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    public String columnName(final int index) {
        return i18nManager.text(I18nKey.FILTER_GRID_COLUMN, letters(index));
    }

    private String letters(final int index) {
        char c = (char) ('A' + (index % ALPHABET_LENGTH));

        if (index / ALPHABET_LENGTH == 0) {
            return Character.toString(c);
        } else {
            return letters(index / ALPHABET_LENGTH - 1) + c;
        }
    }
}

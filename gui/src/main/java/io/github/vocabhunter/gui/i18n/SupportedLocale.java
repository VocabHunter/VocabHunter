/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.i18n;

import io.github.vocabhunter.analysis.core.CoreConstants;

import java.util.Locale;

public enum SupportedLocale {
    ENGLISH(CoreConstants.LOCALE), SPANISH(new Locale("es"));

    public static final SupportedLocale DEFAULT_LOCALE = ENGLISH;

    private final Locale locale;

    SupportedLocale(final Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}

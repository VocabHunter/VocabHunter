/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.i18n;

public enum I18nKey {
    MAIN_WINDOW_UNSAVED("main.window.unsaved"),
    MAIN_WINDOW_UNTITLED("main.window.untitled"),

    ABOUT_VERSION("about.version");

    private final String key;

    I18nKey(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

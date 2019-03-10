/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.i18n;

public enum I18nKey {
    MAIN_WINDOW_UNSAVED("main.window.unsaved"),
    MAIN_WINDOW_UNTITLED("main.window.untitled"),

    ABOUT_VERSION("about.version"),

    ERROR_DETAILS("error.details"),
    ERROR_SESSION_OPEN_DETAILS("error.session.open.details"),
    ERROR_SESSION_OPEN_TITLE("error.session.open.title"),
    ERROR_SESSION_EXPORT_DETAILS("error.session.export.details"),
    ERROR_SESSION_EXPORT_TITLE("error.session.export.title"),
    ERROR_SESSION_SAVE_DETAILS("error.session.save.details"),
    ERROR_SESSION_SAVE_TITLE("error.session.save.title"),

    FILE_NEW("file.new"),
    FILE_OPEN("file.open"),
    FILE_SAVE("file.save"),
    FILE_EXPORT("file.export"),
    FILE_EXCLUDE("file.exclude"),
    FILE_MODIFIED("file.modified"),
    FILE_UNSAVED("file.unsaved"),

    FILE_TYPE_ALL("file.type.all"),
    FILE_TYPE_TEXT("file.type.text"),
    FILE_TYPE_DOCUMENT("file.type.document"),
    FILE_TYPE_ANY_TEXT("file.type.any_text"),
    FILE_TYPE_PDF("file.type.pdf"),
    FILE_TYPE_OFFICE("file.type.office"),
    FILE_TYPE_EBOOK("file.type.ebook"),
    FILE_TYPE_SESSION("file.type.session"),
    FILE_TYPE_SPREADSHEET("file.type.spreadsheet");

    private final String key;

    I18nKey(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

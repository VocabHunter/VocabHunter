/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.i18n;

public enum I18nKey {
    MAIN_WINDOW_UNSAVED("main.window.unsaved"),
    MAIN_WINDOW_UNTITLED("main.window.untitled"),

    SESSION_WORD_USES("session.word.uses"),

    SESSION_TAB_ANALYSIS("session.tab.analysis"),
    SESSION_TAB_PROGRESS("session.tab.progress"),

    SEARCH_MATCH_NONE("search.match.none"),
    SEARCH_MATCH_SELECTION_ON("search.match.selection.on"),
    SEARCH_MATCH_SELECTION_OFF("search.match.selection.off"),

    ABOUT_VERSION("about.version"),

    FILTER_WINDOW_TITLE("filter.window.title"),
    FILTER_WORDS_COUNT("filter.words.count"),
    FILTER_COLUMN("filter.column"),
    FILTER_ERROR_TITLE("filter.error.title"),
    FILTER_ERROR_ALL("filter.error.all"),
    FILTER_ERROR_DISABLED("filter.error.disabled"),

    FILTER_MAIN_LISTS_BUTTON_EDIT("filter.main.lists.button.edit"),
    FILTER_MAIN_LISTS_BUTTON_DELETE("filter.main.lists.button.delete"),
    FILTER_MAIN_LISTS_TYPE_KNOWN("filter.main.lists.type.known"),
    FILTER_MAIN_LISTS_TYPE_BOTH("filter.main.lists.type.both"),
    FILTER_MAIN_LISTS_TYPE_LIST("filter.main.lists.type.list"),

    FILTER_GRID_WINDOW_TITLE("filter.grid.window.title"),
    FILTER_SESSION_WINDOW_TITLE("filter.session.window.title"),
    FILTER_SESSION_TYPE_KNOWN_TIP("filter.session.type.known.tip"),
    FILTER_SESSION_TYPE_UNKNOWN_TIP("filter.session.type.unknown.tip"),

    PROGRESS_SLICE_MARKED("progress.slice.marked"),
    PROGRESS_SLICE_UNMARKED("progress.slice.unmarked"),
    PROGRESS_SLICE_KNOWN("progress.slice.known"),
    PROGRESS_SLICE_UNKNOWN("progress.slice.unknown"),
    PROGRESS_SLICE_FILTERED("progress.slice.filtered"),
    PROGRESS_WORD_COUNT("progress.word.count"),
    PROGRESS_WORD_PERCENTAGE("progress.word.percentage"),

    NOTE_WINDOW_TITLE("note.window.title"),
    NOTE_TITLE("note.title"),

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

    FILE_BUTTON_SAVE("file.button.save"),
    FILE_BUTTON_DISCARD("file.button.discard"),
    FILE_BUTTON_CANCEL("file.button.cancel"),

    FILE_TYPE_ALL("file.type.all"),
    FILE_TYPE_TEXT("file.type.text"),
    FILE_TYPE_DOCUMENT("file.type.document"),
    FILE_TYPE_ANY_TEXT("file.type.any_text"),
    FILE_TYPE_PDF("file.type.pdf"),
    FILE_TYPE_OFFICE("file.type.office"),
    FILE_TYPE_EBOOK("file.type.ebook"),
    FILE_TYPE_SESSION("file.type.session"),
    FILE_TYPE_SPREADSHEET("file.type.spreadsheet"),

    STATUS_ACTION_NEW("status.action.new"),
    STATUS_ACTION_OPEN("status.action.open"),
    STATUS_ACTION_SAVE("status.action.save"),
    STATUS_ACTION_EXPORT("status.action.export"),
    STATUS_ACTION_EXIT("status.action.exit"),
    STATUS_ACTION_ABOUT("status.action.about"),

    STATUS_MARKED_PERCENTAGE("status.marked.percentage"),
    STATUS_POSITION_EDIT_ON("status.position.edit.on"),
    STATUS_POSITION_EDIT_OFF("status.position.edit.off"),

    LANGUAGE_NAME("language.name");

    private final String key;

    I18nKey(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

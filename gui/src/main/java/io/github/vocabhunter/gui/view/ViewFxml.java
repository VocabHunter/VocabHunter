/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

public enum ViewFxml {
    MAIN("main.fxml"),
    SESSION("session.fxml"),
    PROGRESS("progress.fxml"),
    ABOUT("about.fxml"),
    SETTINGS("settings.fxml"),
    FILTER_SESSION("filter-session.fxml"),
    FILTER_GRID("filter-grid.fxml"),
    WORD_NOTE("word-note.fxml");

    private final String name;

    ViewFxml(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

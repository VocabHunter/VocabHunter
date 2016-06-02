/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

public enum SessionTab {
    ANALYSIS("Text Analysis", "tabAnalysis"), PROGRESS("Progress Report", "tabProgress");
    private final String name;

    private final String id;

    SessionTab(final String name, final String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}

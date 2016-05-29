/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class StatusModel {
    private final SimpleStringProperty text = new SimpleStringProperty();

    private final SimpleDoubleProperty progress = new SimpleDoubleProperty();

    public SimpleStringProperty textProperty() {
        return text;
    }

    public void setText(final String text) {
        this.text.set(text);
    }

    public SimpleDoubleProperty progressProperty() {
        return progress;
    }

    public void setProgress(final double progress) {
        this.progress.set(progress);
    }
}

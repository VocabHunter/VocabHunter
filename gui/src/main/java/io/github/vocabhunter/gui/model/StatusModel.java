/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class StatusModel {
    private final SimpleStringProperty text = new SimpleStringProperty();

    private final SimpleDoubleProperty progress = new SimpleDoubleProperty();

    private final SimpleBooleanProperty busy = new SimpleBooleanProperty();

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

    public SimpleBooleanProperty busyProperty() {
        return busy;
    }

    public void setBusy(final boolean busy) {
        this.busy.set(busy);
    }
}

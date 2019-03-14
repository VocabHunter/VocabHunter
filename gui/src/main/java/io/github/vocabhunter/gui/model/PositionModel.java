/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PositionModel {
    private final SimpleIntegerProperty positionIndex = new SimpleIntegerProperty();

    private final SimpleIntegerProperty size = new SimpleIntegerProperty();

    private final SimpleBooleanProperty analysisMode = new SimpleBooleanProperty();

    private final SimpleBooleanProperty editable = new SimpleBooleanProperty(true);

    public SimpleIntegerProperty positionIndexProperty() {
        return positionIndex;
    }

    public int getPositionIndex() {
        return positionIndex.get();
    }

    public void setPositionIndex(final int positionIndex) {
        this.positionIndex.set(positionIndex);
    }

    public SimpleIntegerProperty sizeProperty() {
        return size;
    }

    public int getSize() {
        return size.get();
    }

    public void setSize(final int size) {
        this.size.set(size);
    }

    public SimpleBooleanProperty analysisModeProperty() {
        return analysisMode;
    }

    public void setAnalysisMode(final boolean analysisMode) {
        this.analysisMode.set(analysisMode);
    }

    public boolean isAnalysisMode() {
        return analysisMode.get();
    }

    public SimpleBooleanProperty editableProperty() {
        return editable;
    }

    public boolean isEditable() {
        return editable.get();
    }

    public void setEditable(final boolean editable) {
        this.editable.set(editable);
    }
}

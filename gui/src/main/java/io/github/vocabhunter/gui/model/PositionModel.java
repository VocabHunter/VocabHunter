/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PositionModel {
    private final SimpleIntegerProperty positionIndex = new SimpleIntegerProperty();

    private final SimpleIntegerProperty size = new SimpleIntegerProperty();

    private final SimpleObjectProperty<GuiMode> guiMode = new SimpleObjectProperty<>(GuiMode.EDIT);

    public SimpleIntegerProperty positionIndexProperty() {
        return positionIndex;
    }

    public int getPositionIndex() {
        return positionIndex.get();
    }

    public SimpleIntegerProperty sizeProperty() {
        return size;
    }

    public int getSize() {
        return size.get();
    }

    public GuiMode getGuiMode() {
        return guiMode.get();
    }
}

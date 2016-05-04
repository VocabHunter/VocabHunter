/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import javafx.beans.property.SimpleIntegerProperty;

public class ProgressModel {
    private final SimpleIntegerProperty known = new SimpleIntegerProperty();

    private final SimpleIntegerProperty unknown = new SimpleIntegerProperty();

    private final SimpleIntegerProperty unseenUnfiltered = new SimpleIntegerProperty();

    private final SimpleIntegerProperty unseenFiltered = new SimpleIntegerProperty();

    public SimpleIntegerProperty knownProperty() {
        return known;
    }

    public SimpleIntegerProperty unknownProperty() {
        return unknown;
    }

    public SimpleIntegerProperty unseenUnfilteredProperty() {
        return unseenUnfiltered;
    }

    public SimpleIntegerProperty unseenFilteredProperty() {
        return unseenFiltered;
    }
}

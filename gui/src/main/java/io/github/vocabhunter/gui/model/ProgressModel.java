/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.marked.WordState;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class ProgressModel {
    private final SimpleIntegerProperty known = new SimpleIntegerProperty();

    private final SimpleIntegerProperty unknown = new SimpleIntegerProperty();

    private final SimpleIntegerProperty unseenUnfiltered = new SimpleIntegerProperty();

    private final SimpleIntegerProperty unseenFiltered = new SimpleIntegerProperty();

    private final Map<WordState, SimpleIntegerProperty> properties;

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

    public ProgressModel() {
        Map<WordState, SimpleIntegerProperty> properties = new EnumMap<>(WordState.class);

        properties.put(WordState.KNOWN, known);
        properties.put(WordState.UNKNOWN, unknown);
        properties.put(WordState.UNSEEN, unseenUnfiltered);
        this.properties = Collections.unmodifiableMap(properties);
    }

    public void updateProgress(final int known, final int unknown, final int unseenUnfiltered, final int unseenFiltered) {
        this.known.set(known);
        this.unknown.set(unknown);
        this.unseenUnfiltered.set(unseenUnfiltered);
        this.unseenFiltered.set(unseenFiltered);
    }

    public void updateWord(final WordState oldState, final WordState newState) {
        SimpleIntegerProperty oldValue = properties.get(oldState);
        SimpleIntegerProperty newValue = properties.get(newState);

        updateValue(oldValue, -1);
        updateValue(newValue, 1);
    }

    private void updateValue(final SimpleIntegerProperty property, final int delta) {
        property.set(property.get() + delta);
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.marked.WordState;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.NumberExpression;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class ProgressModel {
    private static final double PERCENT = 100;

    private final SimpleIntegerProperty known = new SimpleIntegerProperty();

    private final SimpleIntegerProperty unknown = new SimpleIntegerProperty();

    private final SimpleIntegerProperty unseenUnfiltered = new SimpleIntegerProperty();

    private final SimpleIntegerProperty unseenFiltered = new SimpleIntegerProperty();

    private final Map<WordState, SimpleIntegerProperty> properties;

    private final NumberBinding marked = known.add(unknown);

    private final NumberBinding totalVisible = known.add(unknown).add(unseenUnfiltered);

    private final NumberBinding total = totalVisible.add(unseenFiltered);

    private final NumberBinding knownPercent = bindPercentage(known, total);

    private final NumberBinding unknownPercent = bindPercentage(unknown, total);

    private final NumberBinding unseenUnfilteredPercent = bindPercentage(unseenUnfiltered, total);

    private final NumberBinding unseenUnfilteredPercentVisible = bindPercentage(unseenUnfiltered, totalVisible);

    private final NumberBinding unseenFilteredPercent = bindPercentage(unseenFiltered, total);

    private final NumberBinding markedPercentVisible = bindPercentage(marked, totalVisible);

    private NumberBinding bindPercentage(final NumberExpression property, final NumberBinding total) {
        return Bindings.when(property.isEqualTo(0)).then(0).otherwise(property.multiply(PERCENT).divide(total));
    }

    public SimpleIntegerProperty knownProperty() {
        return known;
    }

    public SimpleIntegerProperty unknownProperty() {
        return unknown;
    }

    public SimpleIntegerProperty unseenUnfilteredProperty() {
        return unseenUnfiltered;
    }

    public NumberBinding unseenUnfilteredPercentVisibleProperty() {
        return unseenUnfilteredPercentVisible;
    }

    public SimpleIntegerProperty unseenFilteredProperty() {
        return unseenFiltered;
    }

    public NumberBinding markedProperty() {
        return marked;
    }

    public NumberBinding totalProperty() {
        return total;
    }

    public NumberBinding knownPercentProperty() {
        return knownPercent;
    }

    public NumberBinding unknownPercentProperty() {
        return unknownPercent;
    }

    public NumberBinding unseenUnfilteredPercentProperty() {
        return unseenUnfilteredPercent;
    }

    public NumberBinding unseenFilteredPercentProperty() {
        return unseenFilteredPercent;
    }

    public NumberBinding markedPercentVisibleProperty() {
        return markedPercentVisible;
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

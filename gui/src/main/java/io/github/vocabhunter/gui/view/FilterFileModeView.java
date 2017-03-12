/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.gui.model.FilterFileMode;
import javafx.scene.control.Label;

import java.util.EnumMap;
import java.util.Map;

public enum FilterFileModeView {
    SESSION_KNOWN("Known words", null, "filterKnownIcon"),
    SESSION_SEEN("Known & unknown words", "filterKnownIcon", "filterUnknownIcon"),
    GRID("Word list", null, "filterGridIcon");

    private static final Map<FilterFileMode, FilterFileModeView> VIEW_MAP = buildMap();

    private final String name;

    private final String firstIconStyle;

    private final String secondIconStyle;

    FilterFileModeView(final String name, final String firstIconStyle, final String secondIconStyle) {
        this.name = name;
        this.firstIconStyle = firstIconStyle;
        this.secondIconStyle = secondIconStyle;
    }

    public void updateIcons(final Label first, final Label second) {
        updateIcon(first, firstIconStyle);
        updateIcon(second, secondIconStyle);
    }

    private void updateIcon(final Label label, final String style) {
        boolean isVisible = style != null;

        label.setVisible(isVisible);
        label.setManaged(isVisible);
        if (isVisible) {
            FontAwesomeIconView icon = new FontAwesomeIconView();

            icon.setStyleClass(style);
            label.setGraphic(icon);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public static FilterFileModeView getView(final FilterFileMode mode) {
        FilterFileModeView view = VIEW_MAP.get(mode);

        if (view == null) {
            throw new VocabHunterException("Unknown mode " + mode);
        } else {
            return view;
        }
    }

    private static Map<FilterFileMode, FilterFileModeView> buildMap() {
        Map<FilterFileMode, FilterFileModeView> map = new EnumMap<>(FilterFileMode.class);

        map.put(FilterFileMode.SESSION_KNOWN, SESSION_KNOWN);
        map.put(FilterFileMode.SESSION_SEEN, SESSION_SEEN);
        map.put(FilterFileMode.DOCUMENT, GRID);
        map.put(FilterFileMode.EXCEL, GRID);

        return map;
    }
}

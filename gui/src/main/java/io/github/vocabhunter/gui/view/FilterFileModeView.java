/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.github.vocabhunter.gui.model.FilterFileMode;
import javafx.scene.control.Label;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum FilterFileModeView {
    SESSION_KNOWN(FilterFileMode.SESSION_KNOWN, "Known words", null, "filterKnownIcon"), SESSION_SEEN(FilterFileMode.SESSION_SEEN, "Known & unknown words", "filterKnownIcon", "filterUnknownIcon");

    private static final Map<FilterFileMode, FilterFileModeView> VIEW_MAP = Stream.of(FilterFileModeView.values())
        .collect(toMap(FilterFileModeView::getMode, Function.identity()));

    private final FilterFileMode mode;

    private final String name;

    private final String firstIconStyle;

    private final String secondIconStyle;

    FilterFileModeView(final FilterFileMode mode, final String name, final String firstIconStyle, final String secondIconStyle) {
        this.mode = mode;
        this.name = name;
        this.firstIconStyle = firstIconStyle;
        this.secondIconStyle = secondIconStyle;
    }

    public FilterFileMode getMode() {
        return mode;
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
        return VIEW_MAP.get(mode);
    }
}

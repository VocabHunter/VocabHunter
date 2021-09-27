/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.model.FilterFileMode;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.controlsfx.glyphfont.FontAwesome.Glyph;

import java.util.EnumMap;
import java.util.Map;

import static io.github.vocabhunter.gui.i18n.I18nKey.*;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.*;

public enum FilterFileModeView {
    SESSION_KNOWN(FILTER_MAIN_LISTS_TYPE_KNOWN, null, CHECK_CIRCLE, null, "filterKnownIcon"),
    SESSION_SEEN(FILTER_MAIN_LISTS_TYPE_BOTH, CHECK_CIRCLE, TIMES_CIRCLE, "filterKnownIcon", "filterUnknownIcon"),
    GRID(FILTER_MAIN_LISTS_TYPE_LIST, null, TABLE, null, "filterGridIcon");

    private static final Map<FilterFileMode, FilterFileModeView> VIEW_MAP = buildMap();

    private final I18nKey nameKey;

    private final Glyph firstGlyph;

    private final Glyph secondGlyph;

    private final String firstIconStyle;

    private final String secondIconStyle;

    FilterFileModeView(final I18nKey nameKey, final Glyph firstGlyph, final Glyph secondGlyph, final String firstIconStyle, final String secondIconStyle) {
        this.nameKey = nameKey;
        this.firstGlyph = firstGlyph;
        this.secondGlyph = secondGlyph;
        this.firstIconStyle = firstIconStyle;
        this.secondIconStyle = secondIconStyle;
    }

    public void updateIcons(final Label first, final Label second) {
        updateIcon(first, firstGlyph, firstIconStyle);
        updateIcon(second, secondGlyph, secondIconStyle);
    }

    private void updateIcon(final Label label, final Glyph glyph, final String style) {
        boolean isVisible = style != null;

        label.setVisible(isVisible);
        label.setManaged(isVisible);
        if (isVisible) {
            Node icon = IconTool.icon(glyph);

            icon.getStyleClass().add(style);
            label.setGraphic(icon);
        }
    }

    public I18nKey getNameKey() {
        return nameKey;
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

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.model.FilterSessionWord;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;

import static io.github.vocabhunter.gui.i18n.I18nKey.FILTER_SESSION_TYPE_KNOWN_TIP;
import static io.github.vocabhunter.gui.i18n.I18nKey.FILTER_SESSION_TYPE_UNKNOWN_TIP;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.CHECK_CIRCLE;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.TIMES_CIRCLE;

public class FilterSessionStateTableCell extends TableCell<FilterSessionWord, FilterSessionWord> {
    private final Node iconKnown = IconTool.icon(CHECK_CIRCLE);

    private final Node iconUnknown = IconTool.icon(TIMES_CIRCLE);

    private final Tooltip tooltipKnown;

    private final Tooltip tooltipUnknown;

    public FilterSessionStateTableCell(final I18nManager i18nManager) {
        tooltipKnown = new Tooltip(i18nManager.text(FILTER_SESSION_TYPE_KNOWN_TIP));
        tooltipUnknown = new Tooltip(i18nManager.text(FILTER_SESSION_TYPE_UNKNOWN_TIP));

        iconKnown.getStyleClass().add("filterKnownIcon");
        iconUnknown.getStyleClass().add("filterUnknownIcon");
    }

    @Override
    protected void updateItem(final FilterSessionWord item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            setTooltip(null);
            StateClassTool.clearStateClasses(this);
        } else {
            if (item.getState() == WordState.KNOWN) {
                setGraphic(iconKnown);
                setTooltip(tooltipKnown);
            } else {
                setGraphic(iconUnknown);
                setTooltip(tooltipUnknown);
            }
        }
    }
}

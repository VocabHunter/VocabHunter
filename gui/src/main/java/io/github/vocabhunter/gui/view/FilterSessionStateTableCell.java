/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.gui.model.FilterSessionWord;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;

public class FilterSessionStateTableCell extends TableCell<FilterSessionWord, FilterSessionWord> {
    private final FontAwesomeIconView iconKnown = new FontAwesomeIconView();

    private final FontAwesomeIconView iconUnknown = new FontAwesomeIconView();

    private final Tooltip tooltipKnown = new Tooltip("Word marked as known");

    private final Tooltip tooltipUnknown = new Tooltip("Word marked as unknown");

    public FilterSessionStateTableCell() {
        iconKnown.setStyleClass("filterKnownIcon");
        iconUnknown.setStyleClass("filterUnknownIcon");
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

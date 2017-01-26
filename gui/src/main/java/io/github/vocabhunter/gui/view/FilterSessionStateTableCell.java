/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.gui.model.FilterSessionWord;
import javafx.scene.control.TableCell;

public class FilterSessionStateTableCell extends TableCell<FilterSessionWord, FilterSessionWord> {
    private final FontAwesomeIconView iconKnown = new FontAwesomeIconView();

    private final FontAwesomeIconView iconUnknown = new FontAwesomeIconView();

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
            StateClassTool.clearStateClasses(this);
        } else {

            if (item.getState() == WordState.KNOWN) {
                setGraphic(iconKnown);
            } else {
                setGraphic(iconUnknown);
            }
        }
    }
}

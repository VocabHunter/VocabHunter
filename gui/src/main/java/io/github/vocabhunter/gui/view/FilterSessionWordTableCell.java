/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import io.github.vocabhunter.gui.model.FilterSessionWord;
import javafx.scene.control.TableCell;

public class FilterSessionWordTableCell extends TableCell<FilterSessionWord, FilterSessionWord> {
    @Override
    protected void updateItem(final FilterSessionWord item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            StateClassTool.clearStateClasses(this);
        } else {
            setText(item.getWordIdentifier());
            StateClassTool.updateStateClasses(this, item.getState());
        }
    }
}

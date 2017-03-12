/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import io.github.vocabhunter.analysis.grid.GridCell;
import io.github.vocabhunter.analysis.grid.GridLine;
import javafx.scene.control.TableCell;

public class FilterGridWordTableCell extends TableCell<GridLine, GridCell> {
    @Override
    protected void updateItem(final GridCell cell, final boolean isEmpty) {
        super.updateItem(cell, isEmpty);
        if (isEmpty || cell == null) {
            setText(null);
            setGraphic(null);
            StateClassTool.clearExcludedClass(this);
        } else {
            setText(cell.getContent());
            StateClassTool.updatedExcludedClass(this, !cell.isIncluded());
        }
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import io.github.vocabhunter.gui.model.WordModel;
import javafx.scene.control.cell.TextFieldListCell;

public class WordListCell extends TextFieldListCell<WordModel> {
    @Override
    public void updateItem(final WordModel item, final boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            StateClassTool.clearStateClasses(this);
        } else {
            setText(item.getIdentifier());
            StateClassTool.updateStateClasses(this, item.getState());
        }
    }
}

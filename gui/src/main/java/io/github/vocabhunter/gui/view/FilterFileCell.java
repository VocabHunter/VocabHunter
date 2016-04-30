/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import io.github.vocabhunter.gui.model.FilterFileMode;
import io.github.vocabhunter.gui.model.FilterFileModel;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.function.Consumer;

public class FilterFileCell extends ListCell<FilterFileModel> {
    private static final int SPACING = 5;

    private final Button buttonRemoveList = new Button("Remove List");

    private final Label label = new Label();

    private final Pane spacer = new Pane();

    private final ChoiceBox<FilterFileMode> choiceBox = new ChoiceBox<>();

    private final HBox hbox = new HBox(SPACING, label, spacer, choiceBox, buttonRemoveList);

    private FilterFileModel lastItem;

    public FilterFileCell(final Consumer<FilterFileModel> removalHandler) {
        choiceBox.getItems().setAll(FilterFileMode.values());

        HBox.setHgrow(spacer, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER_LEFT);
        buttonRemoveList.setOnAction(e -> removalHandler.accept(lastItem));
        choiceBox.setOnAction(e -> lastItem.setMode(choiceBox.getValue()));
    }

    @Override
    protected void updateItem(final FilterFileModel item, final boolean empty) {
        super.updateItem(item, empty);
        setText(null);

        if (empty || item == null) {
            setGraphic(null);
            lastItem = null;
        } else {
            lastItem = item;
            label.setText(item.getName());
            label.setTooltip(new Tooltip(item.getFile().toString()));
            choiceBox.getSelectionModel().select(item.getMode());
            setGraphic(hbox);
        }
    }
}

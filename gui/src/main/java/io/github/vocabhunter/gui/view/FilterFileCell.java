/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.github.vocabhunter.gui.model.FilterFileModel;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.function.Consumer;

public class FilterFileCell extends ListCell<FilterFileModel> {
    private static final int SPACING = 5;

    private final Label labelName = new Label();

    private final Pane spacer = new Pane();

    private final Label firstIcon = new Label();

    private final Label secondIcon = new Label();

    private final HBox iconBox = new HBox(SPACING, firstIcon, secondIcon);

    private final Label labelType = new Label();

    private final FontAwesomeIconView editIcon = new FontAwesomeIconView();

    private final Button buttonEdit = new Button(null, editIcon);

    private final FontAwesomeIconView removeListIcon = new FontAwesomeIconView();

    private final Button buttonRemoveList = new Button(null, removeListIcon);

    private final HBox hbox = new HBox(SPACING, labelName, spacer, labelType, iconBox, buttonEdit, buttonRemoveList);

    private FilterFileModel lastItem;

    public FilterFileCell(final Consumer<FilterFileModel> removalHandler, final Consumer<FilterFileModel> editHandler) {
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER_LEFT);
        iconBox.setAlignment(Pos.CENTER_LEFT);
        iconBox.getStyleClass().add("iconBox");

        buttonEdit.setOnAction(e -> editHandler.accept(lastItem));
        buttonEdit.setId("buttonEdit");
        buttonEdit.setTooltip(new Tooltip("View/change the filter file"));
        editIcon.setStyleClass("buttonEditIcon");

        buttonRemoveList.setOnAction(e -> removalHandler.accept(lastItem));
        buttonRemoveList.setTooltip(new Tooltip("Remove the filter file"));
        removeListIcon.setStyleClass("buttonDeleteIcon");
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
            labelName.setText(item.getName());
            labelName.setTooltip(new Tooltip(item.getFile().toString()));
            FilterFileModeView modeView = FilterFileModeView.getView(item.getMode());

            labelType.setText(modeView.toString());
            modeView.updateIcons(firstIcon, secondIcon);
            setGraphic(hbox);
        }
    }
}

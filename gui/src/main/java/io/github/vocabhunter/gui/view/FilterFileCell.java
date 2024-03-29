/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.model.FilterFileModel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.function.Consumer;

import static io.github.vocabhunter.gui.i18n.I18nKey.FILTER_MAIN_LISTS_BUTTON_DELETE;
import static io.github.vocabhunter.gui.i18n.I18nKey.FILTER_MAIN_LISTS_BUTTON_EDIT;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.PENCIL;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.TRASH;

public class FilterFileCell extends ListCell<FilterFileModel> {
    private static final int SPACING = 5;

    private final I18nManager i18nManager;

    private final Label labelName = new Label();

    private final Pane spacer = new Pane();

    private final Label firstIcon = new Label();

    private final Label secondIcon = new Label();

    private final HBox iconBox = new HBox(SPACING, firstIcon, secondIcon);

    private final Label labelType = new Label();

    private final Node editIcon = IconTool.icon(PENCIL);

    private final Button buttonEdit = new Button(null, editIcon);

    private final Node removeListIcon = IconTool.icon(TRASH);

    private final Button buttonRemoveList = new Button(null, removeListIcon);

    private final HBox hbox = new HBox(SPACING, labelName, spacer, labelType, iconBox, buttonEdit, buttonRemoveList);

    private FilterFileModel lastItem;

    public FilterFileCell(final I18nManager i18nManager, final Consumer<FilterFileModel> removalHandler, final Consumer<FilterFileModel> editHandler) {
        this.i18nManager = i18nManager;

        HBox.setHgrow(spacer, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER_LEFT);
        iconBox.setAlignment(Pos.CENTER_LEFT);
        iconBox.getStyleClass().add("iconBox");

        buttonEdit.setOnAction(e -> editHandler.accept(lastItem));
        buttonEdit.setId("buttonEdit");
        buttonEdit.setTooltip(new Tooltip(i18nManager.text(FILTER_MAIN_LISTS_BUTTON_EDIT)));
        editIcon.getStyleClass().add("buttonEditIcon");

        buttonRemoveList.setOnAction(e -> removalHandler.accept(lastItem));
        buttonRemoveList.setTooltip(new Tooltip(i18nManager.text(FILTER_MAIN_LISTS_BUTTON_DELETE)));
        removeListIcon.getStyleClass().add("buttonDeleteIcon");
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

            labelType.setText(i18nManager.text(modeView.getNameKey()));
            modeView.updateIcons(firstIcon, secondIcon);
            setGraphic(hbox);
        }
    }
}

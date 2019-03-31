/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.view.ErrorClassTool;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.textfield.CustomTextField;

public class SearchControls {
    private final ToolBar barSearch;

    private final CustomTextField fieldSearch;

    private final Label labelMatches;

    private final Button buttonCloseSearch;

    private final Button buttonSearchUp;

    private final Button buttonSearchDown;

    public SearchControls(
        final ToolBar barSearch, final CustomTextField fieldSearch, final Label labelMatches,
        final Button buttonCloseSearch, final Button buttonSearchUp, final Button buttonSearchDown) {
        this.barSearch = barSearch;
        this.fieldSearch = fieldSearch;
        this.labelMatches = labelMatches;
        this.buttonCloseSearch = buttonCloseSearch;
        this.buttonSearchUp = buttonSearchUp;
        this.buttonSearchDown = buttonSearchDown;
    }

    public StringProperty searchFieldTextProperty() {
        return fieldSearch.textProperty();
    }

    public void bindMatchText(final ObservableValue<String> text) {
        labelMatches.textProperty().bind(text);
    }

    public void setupButtons(final EventHandler<ActionEvent> actionClose, final EventHandler<ActionEvent> actionUp, final EventHandler<ActionEvent> actionDown) {
        buttonCloseSearch.setOnAction(actionClose);
        buttonSearchUp.setOnAction(actionUp);
        buttonSearchDown.setOnAction(actionDown);
        buttonSearchUp.setDisable(true);
        buttonSearchDown.setDisable(true);
    }

    public void setButtonUpDisabled(final boolean isDisabled) {
        buttonSearchUp.setDisable(isDisabled);
    }

    public void setButtonDownDisabled(final boolean isDisabled) {
        buttonSearchDown.setDisable(isDisabled);
    }

    public void selectSearchField() {
        fieldSearch.requestFocus();
    }

    public void clearSearchField() {
        fieldSearch.setText("");
    }

    public void setSearchFailStatus(final boolean isFail) {
        ErrorClassTool.updateClass(fieldSearch, isFail);
    }

    public void setKeyPressHandler(final EventHandler<KeyEvent> keyPressHandler) {
        fieldSearch.setOnKeyPressed(keyPressHandler);
    }

    public void bindSearchOpenProperty(final Property<Boolean> searchOpenProperty) {
        barSearch.visibleProperty().bindBidirectional(searchOpenProperty);
        barSearch.managedProperty().bindBidirectional(searchOpenProperty);
    }
}

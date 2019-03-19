/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.dialogues.FileDialogueFactory;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.model.AbstractFilterModel;
import io.github.vocabhunter.gui.model.FilterFileModel;
import io.github.vocabhunter.gui.view.ErrorClassTool;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static io.github.vocabhunter.gui.i18n.I18nKey.FILTER_WORDS_COUNT;

public abstract class AbstractFilterController<T extends AbstractFilterModel> {
    private final I18nManager i18nManager;

    private final FileDialogueFactory factory;

    @FXML
    private TextField fieldFile;

    @FXML
    private Button buttonChangeFile;

    @FXML
    private Button buttonAddFilterFile;

    @FXML
    private Button buttonCancel;

    @FXML
    private Label labelTotalWords;

    protected AbstractFilterController(final I18nManager i18nManager, final FileDialogueFactory factory) {
        this.i18nManager = i18nManager;
        this.factory = factory;
    }

    public void initialise(final Stage stage, final FilterFileModel parentModel, final Runnable onSave) {
        T model = buildFilterModel(parentModel);

        fieldFile.textProperty().bind(model.filenameProperty());

        buttonChangeFile.setOnAction(e -> changeFile(stage, factory, model));
        buttonAddFilterFile.setOnAction(e -> exit(stage, model, onSave, parentModel, true));
        buttonCancel.setOnAction(e -> exit(stage, model, onSave, parentModel, false));

        labelTotalWords.textProperty().bind(i18nManager.textBinding(FILTER_WORDS_COUNT, model.wordCountProperty()));

        buttonAddFilterFile.disableProperty().bind(model.errorProperty());

        ErrorClassTool.updateClass(labelTotalWords, model.isError());
        model.errorProperty().addListener((o, n, v) -> ErrorClassTool.updateClass(labelTotalWords, v));

        initialiseInternal(parentModel, model);
    }

    protected abstract void changeFile(Stage stage, FileDialogueFactory factory, T filterModel);

    protected abstract T buildFilterModel(FilterFileModel model);

    protected abstract void initialiseInternal(FilterFileModel parentModel, T filterModel);

    protected abstract void exit(Stage stage, T filterModel, final Runnable onSave, FilterFileModel parentModel, boolean isSaveRequested);
}

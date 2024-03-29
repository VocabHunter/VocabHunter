/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.settings.BaseListedFile;
import io.github.vocabhunter.gui.dialogues.FileDialogue;
import io.github.vocabhunter.gui.dialogues.FileDialogueFactory;
import io.github.vocabhunter.gui.dialogues.FileDialogueType;
import io.github.vocabhunter.gui.dialogues.FileFormatType;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.model.*;
import io.github.vocabhunter.gui.services.FilterFileModelTranslator;
import io.github.vocabhunter.gui.services.FilterService;
import io.github.vocabhunter.gui.view.FilterFileCell;
import jakarta.inject.Inject;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static io.github.vocabhunter.gui.common.FieldValueTool.*;

public class SettingsController {
    @FXML
    private TextField fieldMinimumLetters;

    @FXML
    private TextField fieldMinimumOccurrences;

    @FXML
    private CheckBox fieldInitialCapital;

    @FXML
    private ListView<FilterFileModel> listExcludedFiles;

    @FXML
    private Button buttonAddGridFile;

    @FXML
    private Button buttonAddSessionFile;

    @FXML
    private Button buttonOk;

    @FXML
    private Button buttonCancel;

    @Inject
    private I18nManager i18nManager;

    @Inject
    private MainModel model;

    @Inject
    private FileDialogueFactory factory;

    @Inject
    private FilterGridHandler filterGridHandler;

    @Inject
    private FilterSessionHandler filterSessionHandler;

    @Inject
    private FilterFileModelTranslator translator;

    @Inject
    private FilterService filterService;

    private Stage stage;

    private FilterFileListModel filterFilesModel;

    public void initialise(final Stage stage) {
        this.stage = stage;

        buttonOk.setOnAction(e -> exit(true));
        buttonCancel.setOnAction(e -> exit(false));

        FilterSettings settings = model.getFilterSettings();
        initialiseField(fieldMinimumLetters, settings::getMinimumLetters);
        initialiseField(fieldMinimumOccurrences, settings::getMinimumOccurrences);
        initialiseField(fieldInitialCapital, settings::isAllowInitialCapitals);

        List<FilterFileModel> filterFiles = settings.getFilterFiles().stream()
            .map(translator::toModel)
            .toList();

        filterFilesModel = new FilterFileListModel(filterFiles);
        listExcludedFiles.setItems(filterFilesModel.getFiles());
        buttonAddGridFile.setOnAction(e -> processAddGridFile());
        buttonAddSessionFile.setOnAction(e -> processAddSessionFile());
        listExcludedFiles.setCellFactory(p -> new FilterFileCell(i18nManager, filterFilesModel::remove, this::editHandler));
    }

    private void editHandler(final FilterFileModel model) {
        BaseFilterHandler<?> handler = getHandler(model);

        handler.show(model, () -> filterFilesModel.removeIfExists(model));
    }

    private BaseFilterHandler<?> getHandler(final FilterFileModel model) {
        FilterFileMode mode = model.getMode();

        if (mode == FilterFileMode.SESSION_KNOWN || mode == FilterFileMode.SESSION_SEEN) {
            return filterSessionHandler;
        } else {
            return filterGridHandler;
        }
    }

    private void processAddGridFile() {
        FileDialogue dialogue = factory.create(FileDialogueType.OPEN_WORD_LIST, stage);

        dialogue.showChooser();

        if (dialogue.isFileSelected()) {
            FileFormatType format = dialogue.getFileFormatType();
            FilterFileMode mode = FileFormatTypeTool.getMode(format);
            FilterFileModel fileModel = new FilterFileModel(dialogue.getSelectedFile(), mode, FilterGridModel.DEFAULT_COLUMNS);

            showHandler(filterGridHandler, fileModel);
        }
    }

    private void processAddSessionFile() {
        FileDialogue dialogue = factory.create(FileDialogueType.OPEN_SESSION, stage);

        dialogue.showChooser();

        if (dialogue.isFileSelected()) {
            FilterFileModel fileModel = new FilterFileModel(dialogue.getSelectedFile(), FilterFileMode.SESSION_KNOWN);

            showHandler(filterSessionHandler, fileModel);
        }
    }

    private void showHandler(final BaseFilterHandler<?> handler, final FilterFileModel fileModel) {
        handler.show(fileModel, () -> addFile(fileModel));
    }

    private void addFile(final FilterFileModel fileModel) {
        filterFilesModel.removeIfExists(fileModel);
        filterFilesModel.addFile(fileModel);
        listExcludedFiles.scrollTo(fileModel);
    }

    private void exit(final boolean isSaveRequested) {
        if (isSaveRequested) {
            FilterSettings old = model.getFilterSettings();
            int minimumLetters = getAsInteger(fieldMinimumLetters::getText, old.getMinimumLetters());
            int minimumOccurrences = getAsInteger(fieldMinimumOccurrences::getText, old.getMinimumOccurrences());
            boolean allowInitialCapitals = fieldInitialCapital.isSelected();
            List<BaseListedFile> filterFiles = filterFilesModel.getFiles().stream()
                .map(translator::fromModel)
                .toList();

            FilterSettings settings = new FilterSettings(minimumLetters, minimumOccurrences, allowInitialCapitals, filterFiles);

            filterService.setFilterSettings(settings);
            model.setEnableFilters(true);
        }
        stage.close();
    }

    private void initialiseField(final TextField field, final Supplier<Object> settingGetter) {
        StringProperty textProperty = field.textProperty();
        ReadOnlyBooleanProperty focusedProperty = field.focusedProperty();

        field.setText(settingGetter.get().toString());
        textProperty.addListener((o, oldValue, newValue) -> cleanNonNegativeInteger(field::setText, newValue, oldValue));
        focusedProperty.addListener((o, old, isFocused) -> applyDefaultIfEmpty(field::setText, field::getText, settingGetter));
    }

    private void initialiseField(final CheckBox field, final BooleanSupplier settingGetter) {
        boolean value = settingGetter.getAsBoolean();

        field.setSelected(value);
    }
}

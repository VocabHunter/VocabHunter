/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.vocabhunter.gui.dialogues.FileDialogue;
import io.github.vocabhunter.gui.dialogues.FileDialogueFactory;
import io.github.vocabhunter.gui.dialogues.FileDialogueType;
import io.github.vocabhunter.gui.model.*;
import io.github.vocabhunter.gui.view.FilterFileCell;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.inject.Inject;

import static io.github.vocabhunter.gui.common.FieldValueTool.*;

@SuppressFBWarnings({"NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD", "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"})
public class SettingsController {
    public TextField fieldMinimumLetters;

    public TextField fieldMinimumOccurrences;

    public CheckBox fieldInitialCapital;

    public ListView<FilterFileModel> listExcludedFiles;

    public Button buttonAddSessionFile;

    public Button buttonOk;

    public Button buttonCancel;

    @Inject
    private MainModel model;

    @Inject
    private FileDialogueFactory factory;

    @Inject
    private FilterSessionHandler filterSessionHandler;

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
            .map(f -> new FilterFileModel(f.getFile(), f.getMode()))
            .collect(Collectors.toList());

        filterFilesModel = new FilterFileListModel(filterFiles);
        listExcludedFiles.setItems(filterFilesModel.getFiles());
        buttonAddSessionFile.setOnAction(e -> processAddSessionFile());
        listExcludedFiles.setCellFactory(p -> new FilterFileCell(filterFilesModel::remove, this::editHandler));
    }

    private void editHandler(final FilterFileModel model) {
        filterSessionHandler.show(model, () -> filterFilesModel.removeIfExists(model));
    }

    private void processAddSessionFile() {
        FileDialogue dialogue = factory.create(FileDialogueType.OPEN_SESSION, stage);

        dialogue.showChooser();

        if (dialogue.isFileSelected()) {
            FilterFileModel fileModel = new FilterFileModel(dialogue.getSelectedFile());

            filterSessionHandler.show(fileModel, () -> addFile(fileModel));
        }
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
            List<FilterFile> filterFiles = filterFilesModel.getFiles().stream()
                .map(f -> new FilterFile(f.getFile(), f.getMode()))
                .collect(Collectors.toList());

            FilterSettings settings = new FilterSettings(minimumLetters, minimumOccurrences, allowInitialCapitals, filterFiles);

            model.setFilterSettings(settings);
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

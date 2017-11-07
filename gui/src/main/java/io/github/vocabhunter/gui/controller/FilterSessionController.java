/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.session.SessionWordsTool;
import io.github.vocabhunter.gui.dialogues.FileDialogue;
import io.github.vocabhunter.gui.dialogues.FileDialogueFactory;
import io.github.vocabhunter.gui.dialogues.FileDialogueType;
import io.github.vocabhunter.gui.model.FilterFileMode;
import io.github.vocabhunter.gui.model.FilterFileModel;
import io.github.vocabhunter.gui.model.FilterSessionModel;
import io.github.vocabhunter.gui.model.FilterSessionWord;
import io.github.vocabhunter.gui.view.FilterSessionStateTableCell;
import io.github.vocabhunter.gui.view.FilterSessionWordTableCell;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.nio.file.Path;
import java.util.List;
import javax.inject.Inject;

public class FilterSessionController extends AbstractFilterController<FilterSessionModel> {
    private static final Callback<CellDataFeatures<FilterSessionWord, FilterSessionWord>, ObservableValue<FilterSessionWord>> WORD_SELF_FACTORY
        = x -> x.getValue().selfProperty();

    private final SessionWordsTool sessionWordsTool;

    @FXML
    private RadioButton buttonKnown;

    @FXML
    private RadioButton buttonSeen;

    @FXML
    private TableView<FilterSessionWord> tableWords;

    @FXML
    private TableColumn<FilterSessionWord, FilterSessionWord> columnType;

    @FXML
    private TableColumn<FilterSessionWord, FilterSessionWord> columnWord;

    @Inject
    public FilterSessionController(final FileDialogueFactory factory, final SessionWordsTool sessionWordsTool) {
        super(factory);
        this.sessionWordsTool = sessionWordsTool;
    }

    @Override
    protected FilterSessionModel buildFilterModel(final FilterFileModel model) {
        Path file = model.getFile();
        List<? extends MarkedWord> words = sessionWordsTool.readMarkedWords(file);

        return new FilterSessionModel(file, words);
    }

    @Override
    protected void exit(final Stage stage, final FilterSessionModel filterModel, final Runnable onSave, final FilterFileModel parentModel, final boolean isSaveRequested) {
        if (isSaveRequested) {
            FilterFileMode mode = filterModel.isIncludeUnknown() ? FilterFileMode.SESSION_SEEN : FilterFileMode.SESSION_KNOWN;

            parentModel.setMode(mode);
            parentModel.setFile(filterModel.getFile());
            onSave.run();
        }
        stage.close();
    }

    @Override
    protected void initialiseInternal(final FilterFileModel parentModel, final FilterSessionModel filterModel) {
        buildToggleGroup(parentModel, filterModel);
        prepareTable(filterModel);
    }

    private void buildToggleGroup(final FilterFileModel parentModel, final FilterSessionModel filterModel) {
        ToggleGroup toggleGroup = new ToggleGroup();

        buttonKnown.setToggleGroup(toggleGroup);
        buttonSeen.setToggleGroup(toggleGroup);

        boolean isIncludeUnknown = parentModel.getMode() == FilterFileMode.SESSION_SEEN;

        buttonKnown.setSelected(!isIncludeUnknown);
        buttonSeen.setSelected(isIncludeUnknown);
        filterModel.includeUnknownProperty().bind(buttonSeen.selectedProperty());
    }

    private void prepareTable(final FilterSessionModel filterModel) {
        tableWords.setItems(filterModel.getSeenWords());
        tableWords.setSelectionModel(null);

        columnType.setCellValueFactory(WORD_SELF_FACTORY);
        columnType.setCellFactory(c -> new FilterSessionStateTableCell());
        columnType.setSortable(false);

        columnWord.setCellValueFactory(WORD_SELF_FACTORY);
        columnWord.setCellFactory(c -> new FilterSessionWordTableCell(filterModel.includeUnknownProperty()));
        columnWord.setSortable(false);

        filterModel.includeUnknownProperty().addListener((t, o, v) -> tableWords.refresh());
    }

    @Override
    protected void changeFile(final Stage stage, final FileDialogueFactory factory, final FilterSessionModel filterModel) {
        FileDialogue dialogue = factory.create(FileDialogueType.OPEN_SESSION, stage);

        dialogue.showChooser();

        if (dialogue.isFileSelected()) {
            Path file = dialogue.getSelectedFile();
            List<? extends MarkedWord> words = sessionWordsTool.readMarkedWords(file);

            filterModel.replaceContent(file, words);
            buttonKnown.setSelected(true);
        }
    }
}

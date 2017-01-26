/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.session.SessionWordsTool;
import io.github.vocabhunter.gui.model.FilterFileMode;
import io.github.vocabhunter.gui.model.FilterFileModel;
import io.github.vocabhunter.gui.model.FilterSessionModel;
import io.github.vocabhunter.gui.model.FilterSessionWord;
import io.github.vocabhunter.gui.view.FilterSessionStateTableCell;
import io.github.vocabhunter.gui.view.FilterSessionWordTableCell;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;

@SuppressFBWarnings({"NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD", "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"})
public class FilterSessionController {
    private static final Callback<TableColumn.CellDataFeatures<FilterSessionWord, FilterSessionWord>, ObservableValue<FilterSessionWord>> WORD_SELF_FACTORY
        = x -> x.getValue().selfProperty();

    public TextField fieldFile;

    public RadioButton buttonKnown;

    public RadioButton buttonSeen;

    public Button buttonAddFilterFile;

    public Button buttonCancel;

    public Label labelTotalWords;

    public TableView<FilterSessionWord> tableWords;

    public TableColumn<FilterSessionWord, FilterSessionWord> columnType;

    public TableColumn<FilterSessionWord, FilterSessionWord> columnWord;

    private Stage stage;

    private FilterSessionModel model;

    private Runnable onSave;

    public void initialise(final Stage stage, final FilterFileModel model, final Runnable onSave) {
        this.stage = stage;
        this.onSave = onSave;
        this.model = buildFilterSessionModel(model);

        buildToggleGroup();
        fieldFile.setText(model.getName());

        buttonAddFilterFile.setOnAction(e -> exit(true));
        buttonCancel.setOnAction(e -> exit(false));

        bindTotalWordsLabel();
        prepareTable();
    }

    private FilterSessionModel buildFilterSessionModel(final FilterFileModel model) {
        Path file = model.getFile();
        List<? extends MarkedWord> words = SessionWordsTool.readMarkedWords(file);

        return new FilterSessionModel(words, model);
    }

    private void exit(final boolean isSaveRequested) {
        if (isSaveRequested) {
            FilterFileMode mode = FilterFileMode.getMode(model.isIncludeUnknown());

            model.getFilterFileModel().setMode(mode);
            onSave.run();
        }
        stage.close();
    }

    private void buildToggleGroup() {
        ToggleGroup toggleGroup = new ToggleGroup();

        buttonKnown.setToggleGroup(toggleGroup);
        buttonSeen.setToggleGroup(toggleGroup);

        boolean isIncludeUnknown = model.getFilterFileModel().getMode().isIncludeUnknown();

        buttonKnown.setSelected(!isIncludeUnknown);
        buttonSeen.setSelected(isIncludeUnknown);
        model.includeUnknownProperty().bind(buttonSeen.selectedProperty());
    }

    private void bindTotalWordsLabel() {
        StringBinding binding = Bindings.createStringBinding(() -> formatTotalWords(), model.includeUnknownProperty());

        labelTotalWords.textProperty().bind(binding);
    }

    private String formatTotalWords() {
        long count;

        if (model.isIncludeUnknown()) {
            count = model.getSeenCount();
        } else {
            count = model.getKnownCount();
        }

        return MessageFormat.format("Total words: {0}", count);
    }

    private void prepareTable() {
        tableWords.setItems(model.getSeenWords());
        tableWords.setSelectionModel(null);

        columnType.setCellValueFactory(WORD_SELF_FACTORY);
        columnType.setCellFactory(c -> new FilterSessionStateTableCell());
        columnType.setSortable(false);

        columnWord.setCellValueFactory(WORD_SELF_FACTORY);
        columnWord.setCellFactory(c -> new FilterSessionWordTableCell());
        columnWord.setSortable(false);
    }
}

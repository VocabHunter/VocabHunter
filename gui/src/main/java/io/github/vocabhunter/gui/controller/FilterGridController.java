/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.grid.GridCell;
import io.github.vocabhunter.analysis.grid.GridLine;
import io.github.vocabhunter.analysis.grid.TextGrid;
import io.github.vocabhunter.analysis.grid.TextGridManager;
import io.github.vocabhunter.gui.common.ColumnNameTool;
import io.github.vocabhunter.gui.dialogues.FileDialogue;
import io.github.vocabhunter.gui.dialogues.FileDialogueFactory;
import io.github.vocabhunter.gui.dialogues.FileDialogueType;
import io.github.vocabhunter.gui.dialogues.FileFormatType;
import io.github.vocabhunter.gui.model.FilterFileMode;
import io.github.vocabhunter.gui.model.FilterFileModel;
import io.github.vocabhunter.gui.model.FilterGridModel;
import io.github.vocabhunter.gui.view.FilterGridWordTableCell;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

public class FilterGridController extends AbstractFilterController<FilterGridModel> {
    private static final int MAX_COLUMNS_WITHOUT_SCROLL = 3;

    private static final int PREFERRED_COLUMN_WIDTH = 200;

    private static final int MINIMUM_MULTI_COLUMN_COLUMN_BOX_HEIGHT = 50;

    private final TextGridManager textGridManager;

    @FXML
    private TableView<GridLine> tableWords;

    @FXML
    private ScrollPane columnSelectionBoxScrollPane;

    @FXML
    private VBox columnSelectionBox;

    @FXML
    private List<CheckBox> checkBoxes;

    private final Map<GridCell, ReadOnlyObjectWrapper<GridCell>> cellCache = new HashMap<>();

    @Inject
    public FilterGridController(final FileDialogueFactory factory, final TextGridManager textGridManager) {
        super(factory);
        this.textGridManager = textGridManager;
    }

    @Override
    protected FilterGridModel buildFilterModel(final FilterFileModel model) {
        Path file = model.getFile();
        FilterFileMode mode = model.getMode();
        TextGrid grid = readGrid(file, mode);

        return new FilterGridModel(file, grid, mode, model.getColumns());
    }

    @Override
    protected void exit(final Stage stage, final FilterGridModel filterModel, final Runnable onSave, final FilterFileModel parentModel, final boolean isSaveRequested) {
        if (isSaveRequested) {
            parentModel.setMode(filterModel.getMode());
            parentModel.setFile(filterModel.getFile());
            parentModel.setColumns(filterModel.getColumns());
            onSave.run();
        }
        stage.close();
    }

    @Override
    protected void initialiseInternal(final FilterFileModel parentModel, final FilterGridModel filterModel) {
        tableWords.setItems(filterModel.getLines());
        tableWords.setSelectionModel(null);
        setupColumnsAndCheckBoxes(filterModel);
    }

    private ObservableValue<GridCell> extractValue(final CellDataFeatures<GridLine, GridCell> features, final int index) {
        List<GridCell> cells = features.getValue().getCells();
        GridCell cell = getCell(cells, index);

        return cellCache.computeIfAbsent(cell, ReadOnlyObjectWrapper::new);
    }

    private GridCell getCell(final List<GridCell> cells, final int index) {
        if (index < cells.size()) {
            return cells.get(index);
        } else {
            return GridCell.EMPTY_CELL;
        }
    }

    @Override
    protected void changeFile(final Stage stage, final FileDialogueFactory factory, final FilterGridModel filterModel) {
        FileDialogue dialogue = factory.create(FileDialogueType.OPEN_WORD_LIST, stage);

        dialogue.showChooser();

        if (dialogue.isFileSelected()) {
            Path file = dialogue.getSelectedFile();
            FileFormatType format = dialogue.getFileFormatType();
            FilterFileMode mode = FileFormatTypeTool.getMode(format);
            TextGrid grid = readGrid(file, mode);

            unbindCheckboxes(filterModel);
            filterModel.replaceContent(file, grid, mode, FilterGridModel.DEFAULT_COLUMNS);
            setupColumnsAndCheckBoxes(filterModel);
        }
    }

    private TextGrid readGrid(final Path file, final FilterFileMode mode) {
        if (mode == FilterFileMode.DOCUMENT) {
            return textGridManager.readDocument(file);
        } else {
            return textGridManager.readExcel(file);
        }
    }

    private void unbindCheckboxes(final FilterGridModel filterModel) {
        IntStream.range(0, checkBoxes.size())
            .forEach(i -> unbindCheckbox(filterModel, i));
    }

    private void unbindCheckbox(final FilterGridModel filterModel, final int column) {
        checkBoxes.get(column).selectedProperty().unbindBidirectional(getColumnSelection(filterModel, column));
    }

    private void setupColumnsAndCheckBoxes(final FilterGridModel filterModel) {
        checkBoxes = buildAndBindCheckBoxes(filterModel);
        tableWords.getColumns().setAll(buildColumns(filterModel));
        columnSelectionBox.getChildren().setAll(checkBoxes);
        if (isScrollableColumnList(filterModel)) {
            tableWords.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        }
        if (filterModel.getColumnCount() > 1) {
            columnSelectionBoxScrollPane.setMinHeight(MINIMUM_MULTI_COLUMN_COLUMN_BOX_HEIGHT);
        }
    }

    private List<CheckBox> buildAndBindCheckBoxes(final FilterGridModel filterModel) {
        return IntStream.range(0, filterModel.getColumnCount())
            .mapToObj(i -> buildAndBindCheckBox(filterModel, i))
            .collect(toList());
    }

    private CheckBox buildAndBindCheckBox(final FilterGridModel filterModel, final int columnNo) {
        String name = ColumnNameTool.columnName(columnNo);
        CheckBox box = new CheckBox(name);
        BooleanProperty property = getColumnSelection(filterModel, columnNo);

        box.selectedProperty().bindBidirectional(property);
        box.setId("checkBoxColumn" + columnNo);
        box.selectedProperty().addListener((a, b, c) -> tableWords.refresh());

        return box;
    }

    private List<TableColumn<GridLine, GridCell>> buildColumns(final FilterGridModel filterModel) {
        return IntStream.range(0, filterModel.getColumnCount())
            .mapToObj(i -> buildColumn(filterModel, i))
            .collect(toList());
    }

    private TableColumn<GridLine, GridCell> buildColumn(final FilterGridModel filterModel, final int index) {
        TableColumn<GridLine, GridCell> column = new TableColumn<>(ColumnNameTool.columnName(index));

        column.setSortable(false);
        column.setCellValueFactory(features -> extractValue(features, index));
        column.setCellFactory(c -> new FilterGridWordTableCell(filterModel.getColumnSelections().get(index)));
        if (isScrollableColumnList(filterModel)) {
            column.setPrefWidth(PREFERRED_COLUMN_WIDTH);
        }

        return column;
    }

    private boolean isScrollableColumnList(final FilterGridModel filterModel) {
        return filterModel.getColumnCount() > MAX_COLUMNS_WITHOUT_SCROLL;
    }

    private BooleanProperty getColumnSelection(final FilterGridModel filterModel, final int columnNo) {
        return filterModel.getColumnSelections().get(columnNo);
    }
}

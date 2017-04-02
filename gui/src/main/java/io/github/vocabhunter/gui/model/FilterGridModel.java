/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.grid.GridLine;
import io.github.vocabhunter.analysis.grid.TextGrid;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class FilterGridModel extends AbstractFilterModel {
    public static final Set<Integer> DEFAULT_COLUMNS = Collections.singleton(0);

    private final ObservableList<GridLine> lines = FXCollections.observableArrayList();

    private final SimpleIntegerProperty count = new SimpleIntegerProperty();

    private TextGrid grid;

    private FilterFileMode mode;

    private final ObservableList<BooleanProperty> columnSelections = FXCollections.observableArrayList();

    public FilterGridModel(final Path file, final TextGrid grid, final FilterFileMode mode, final Set<Integer> columns) {
        super(file);
        setupValues(grid, mode, columns);
        bindValues();
    }

    public void replaceContent(final Path file, final TextGrid grid, final FilterFileMode mode, final Set<Integer> columns) {
        replaceContent(file);
        setupValues(grid, mode, columns);
    }

    private void setupValues(final TextGrid grid, final FilterFileMode mode, final Set<Integer> columns) {
        this.grid = grid;
        this.mode = mode;
        lines.setAll(grid.getLines());

        columnSelections.setAll(buildColumnProperties(columns));
        count.unbind();
        count.bind(Bindings.createIntegerBinding(this::calculateCount, selectionsArray()));
    }

    private BooleanProperty[] selectionsArray() {
        return columnSelections.stream()
            .toArray(BooleanProperty[]::new);
    }

    private List<BooleanProperty> buildColumnProperties(final Set<Integer> columns) {
        return columnIndexStream()
            .mapToObj(i -> new SimpleBooleanProperty(columns.contains(i)))
            .collect(toList());
    }

    @Override
    protected ObservableIntegerValue countValue() {
        return count;
    }

    private int calculateCount() {
        return columnIndexStream()
            .filter(this::isSelectedColumn)
            .map(i -> grid.getColumns().get(i).getAcceptedCount())
            .sum();
    }

    public ObservableList<GridLine> getLines() {
        return lines;
    }

    public FilterFileMode getMode() {
        return mode;
    }

    public ObservableList<BooleanProperty> getColumnSelections() {
        return columnSelections;
    }

    public Set<Integer> getColumns() {
        return columnIndexStream()
            .filter(this::isSelectedColumn)
            .boxed()
            .collect(Collectors.toSet());
    }

    private boolean isSelectedColumn(final int i) {
        return columnSelections.get(i).get();
    }

    private IntStream columnIndexStream() {
        return IntStream.range(0, getColumnCount());
    }

    public int getColumnCount() {
        return grid.getColumns().size();
    }
}

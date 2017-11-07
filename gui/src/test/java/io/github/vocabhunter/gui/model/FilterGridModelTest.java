/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.grid.GridTestTool;
import io.github.vocabhunter.analysis.grid.TextGrid;
import javafx.beans.property.BooleanProperty;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterGridModelTest extends BaseFilterModelTest {
    private static final TextGrid EMPTY_GRID = GridTestTool.emptyGrid();

    private static final TextGrid NORMAL_GRID = GridTestTool.grid();

    @Test
    public void testEmpty() {
        FilterGridModel target = build(EMPTY_GRID);

        validateError(target, EMPTY_GRID);
    }

    @Test
    public void testFirstColumn() {
        FilterGridModel target = build(NORMAL_GRID, 0);

        validateOk(target, NORMAL_GRID, "2", 0);
    }

    @Test
    public void testAllColumns() {
        FilterGridModel target = build(NORMAL_GRID, 0, 1, 2);

        validateOk(target, NORMAL_GRID, "3", 0, 1, 2);
    }

    @Test
    public void testSelectColumn() {
        FilterGridModel target = build(NORMAL_GRID);

        target.getColumnSelections().get(0).set(true);

        validateOk(target, NORMAL_GRID, "2", 0);
    }

    @Test
    public void testReplaceSimple() {
        FilterGridModel target = build(EMPTY_GRID);

        target.replaceContent(FILE_2, NORMAL_GRID, FilterFileMode.EXCEL, columns(0));

        validateReplaceOk(target, NORMAL_GRID, "2", FilterFileMode.EXCEL, 0);
    }

    @Test
    public void testReplaceAndSelect() {
        FilterGridModel target = build(EMPTY_GRID);

        target.replaceContent(FILE_2, NORMAL_GRID, FilterFileMode.EXCEL, columns());
        target.getColumnSelections().get(0).set(true);

        validateReplaceOk(target, NORMAL_GRID, "2", FilterFileMode.EXCEL, 0);
    }

    @Test
    public void testReplaceError() {
        FilterGridModel target = build(NORMAL_GRID, 0);

        target.replaceContent(FILE_2, EMPTY_GRID, FilterFileMode.EXCEL, columns());

        validateReplaceError(target, EMPTY_GRID, FilterFileMode.EXCEL);
    }

    private void validateOk(final FilterGridModel target, final TextGrid grid, final String countDescription, final Integer... columns) {
        validate(target, FILE_1, FILENAME_1, countDescription, false, grid, FilterFileMode.DOCUMENT, columns(columns));
    }

    private void validateError(final FilterGridModel target, final TextGrid grid, final Integer... columns) {
        validate(target, FILE_1, FILENAME_1, AbstractFilterModel.ERROR, true, grid, FilterFileMode.DOCUMENT, columns(columns));
    }

    private void validateReplaceOk(final FilterGridModel target, final TextGrid grid, final String countDescription, final FilterFileMode mode, final Integer... columns) {
        validate(target, FILE_2, FILENAME_2, countDescription, false, grid, mode, columns(columns));
    }

    private void validateReplaceError(final FilterGridModel target, final TextGrid grid, final FilterFileMode mode, final Integer... columns) {
        validate(target, FILE_2, FILENAME_2, AbstractFilterModel.ERROR, true, grid, mode, columns(columns));
    }

    private void validate(
        final FilterGridModel target, final Path file, final String filename,
        final String countDescription, final boolean isError,
        final TextGrid grid, final FilterFileMode mode, final Set<Integer> columns) {

        assertAll(
            () -> {
                int columnCount = grid.getColumns().size();

                assertAll(
                    () -> assertEquals(columnCount, target.getColumnCount(), "Column count"),
                    () -> validateColumnSelections(target, columns, columnCount)
                );
            },
            () -> assertEquals(grid.getLines(), target.getLines(), "Lines"),
            () -> assertEquals(mode, target.getMode(), "Mode"),
            () -> assertEquals(columns, target.getColumns(), "Columns"),
            () -> validateCommon(target, file, filename, countDescription, isError)
        );
    }

    private void validateColumnSelections(final FilterGridModel target, final Set<Integer> columns, final int columnCount) {
        List<BooleanProperty> columnSelections = target.getColumnSelections();

        assertAll(
            () -> assertEquals(columnCount, columnSelections.size()),
            () -> IntStream.range(0, columnCount)
                       .forEach(i -> assertEquals(columns.contains(i), columnSelections.get(i).get()))
        );
    }

    private FilterGridModel build(final TextGrid grid, final Integer... columns) {
        return new FilterGridModel(FILE_1, grid, FilterFileMode.DOCUMENT, columns(columns));
    }

    private Set<Integer> columns(final Integer... columns) {
        return new TreeSet<>(listOf(columns));
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public final class GridTestTool {
    private GridTestTool() {
        // Prevent instantiation - all members are static
    }

    public static TextGrid emptyGrid() {
        return new TextGrid(Collections.emptyList(), Collections.emptyList());
    }

    public static TextGrid grid() {
        return new TextGrid(normalisedLines(), columns(2, 0, 1));
    }

    public static List<GridLine> normalisedLines() {
        GridLine line1 = new GridLine(acceptedCell("A1"), rejectedCell("B1"));
        GridLine line2 = new GridLine(acceptedCell("A2"));
        GridLine line3 = new GridLine(acceptedCell(""), acceptedCell(""), acceptedCell("C3"));

        return asList(line1, line2, line3);
    }

    public static List<GridLine> unnormalisedLines() {
        GridLine line1 = new GridLine(acceptedCell("A1"), rejectedCell("B1"));
        GridLine line2 = new GridLine(acceptedCell("A2"));
        GridLine line3 = new GridLine(acceptedCell(""), acceptedCell(""), acceptedCell("C3"), acceptedCell(""));
        GridLine line4 = new GridLine(acceptedCell(""));
        GridLine line5 = new GridLine();

        return asList(line1, line2, line3, line4, line5);
    }

    public static TextGrid legalSpacingGrid() {
        return new TextGrid(legalSpacingLines(), columns(2));
    }

    public static List<GridLine> legalSpacingLines() {
        GridLine line1 = new GridLine();
        GridLine line2 = new GridLine(acceptedCell("Content1"));
        GridLine line3 = new GridLine();
        GridLine line4 = new GridLine(acceptedCell("Content2"));

        return asList(line1, line2, line3, line4);
    }

    private static List<GridColumn> columns(final int... columns) {
        return IntStream.of(columns)
            .mapToObj(GridColumn::new)
            .collect(toList());
    }

    public static GridCell acceptedCell(final String content) {
        return new GridCell(content, false);
    }

    public static GridCell rejectedCell(final String content) {
        return new GridCell(content, true);
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static java.util.stream.Collectors.toList;

public final class GridTestTool {
    private GridTestTool() {
        // Prevent instantiation - all members are static
    }

    public static TextGrid emptyGrid() {
        return new TextGrid(Collections.emptyList(), Collections.emptyList());
    }

    public static TextGrid grid() {
        return new TextGrid(normalisedGridLines(), columns(2, 0, 1));
    }

    public static List<GridLine> documentLines() {
        GridLine line1 = new GridLine(acceptedCell("Accepted1"));
        GridLine line2 = new GridLine(rejectedCell("Rejected1"));
        GridLine line3 = new GridLine(acceptedCell("Accepted2"));

        return listOf(line1, line2, line3);
    }

    public static List<GridLine> normalisedGridLines() {
        GridLine line1 = new GridLine(acceptedCell("A1"), rejectedCell("B1"));
        GridLine line2 = new GridLine(acceptedCell("A2"));
        GridLine line3 = new GridLine(acceptedCell(""), acceptedCell(""), acceptedCell("C3"));

        return listOf(line1, line2, line3);
    }

    public static List<GridLine> unnormalisedGridLines() {
        GridLine line1 = new GridLine(acceptedCell("A1"), rejectedCell("B1"));
        GridLine line2 = new GridLine(acceptedCell("A2"));
        GridLine line3 = new GridLine(acceptedCell(""), acceptedCell(""), acceptedCell("C3"), acceptedCell(""));
        GridLine line4 = new GridLine(acceptedCell(""));
        GridLine line5 = new GridLine();

        return listOf(line1, line2, line3, line4, line5);
    }

    public static TextGrid legalSpacingGrid() {
        return new TextGrid(legalSpacingGridLines(), columns(2));
    }

    public static List<GridLine> legalSpacingGridLines() {
        GridLine line1 = new GridLine();
        GridLine line2 = new GridLine(acceptedCell("Content1"));
        GridLine line3 = new GridLine();
        GridLine line4 = new GridLine(acceptedCell("Content2"));

        return listOf(line1, line2, line3, line4);
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

    public static Path getFile(final String fileName) throws Exception {
        URL resource = GridTestTool.class.getResource("/" + fileName);

        return Paths.get(resource.toURI());
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.unmodifiableList;

public final class TextGrid {
    private final List<GridLine> lines;

    private final List<GridColumn> columns;

    public TextGrid(final List<GridLine> lines, final List<GridColumn> columns) {
        this.lines = new ArrayList<>(lines);
        this.columns = new ArrayList<>(columns);
    }

    public List<GridColumn> getColumns() {
        return unmodifiableList(columns);
    }

    public List<GridLine> getLines() {
        return unmodifiableList(lines);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TextGrid textGrid = (TextGrid) o;

        return new EqualsBuilder()
            .append(lines, textGrid.lines)
            .append(columns, textGrid.columns)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(lines)
            .append(columns)
            .toHashCode();
    }

    @Override
    public String toString() {
        String header = IntStream.range(0, columns.size())
            .mapToObj(i -> toString(i, columns.get(i)))
            .collect(Collectors.joining(","));
        String textBody = lines.stream()
            .map(GridLine::toString)
            .collect(Collectors.joining("\n"));

        return header + ":\n" + textBody;
    }

    private String toString(final int index, final GridColumn column) {
        return String.format("Column %s (%s accepted)", index + 1, column.getAcceptedCount());
    }
}

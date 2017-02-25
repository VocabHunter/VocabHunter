/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.joining;

public final class GridLine {
    private final List<GridCell> cells;

    public GridLine(final GridCell... cells) {
        this(Arrays.asList(cells));
    }

    public GridLine(final List<GridCell> cells) {
        this.cells = new ArrayList<>(cells);
    }

    public List<GridCell> getCells() {
        return Collections.unmodifiableList(cells);
    }

    public GridCell getCell(final int index) {
        if (index < cells.size()) {
            return cells.get(index);
        } else {
            return GridCell.EMPTY_CELL;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GridLine gridLine = (GridLine) o;

        return new EqualsBuilder()
            .append(cells, gridLine.cells)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(cells)
            .toHashCode();
    }

    @Override
    public String toString() {
        return cells.stream()
            .map(GridCell::toString)
            .collect(joining(","));
    }
}

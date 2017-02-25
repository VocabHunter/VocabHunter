/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.analysis.core.CoreTool;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class TextGridBuilderImpl implements TextGridBuilder {
    @Override
    public TextGrid build(final List<GridLine> lines) {
        List<GridLine> normalised = normalise(lines);
        Map<Integer, Integer> counts = countMap(normalised);
        List<GridColumn> columns = counts.values().stream()
            .map(GridColumn::new)
            .collect(toList());

        return new TextGrid(normalised, columns);
    }

    private List<GridLine> normalise(final List<GridLine> lines) {
        List<GridLine> normalised = lines.stream()
            .map(this::normalise)
            .collect(toList());
        int last = CoreTool.findLast(normalised, l -> !l.getCells().isEmpty())
            .orElse(-1) + 1;

        return normalised.subList(0, last);
    }

    private GridLine normalise(final GridLine input) {
        List<GridCell> cells = input.getCells();
        int fullCount = cells.size();
        int newCount = CoreTool.findLast(cells, c -> !c.isEmpty())
            .orElse(-1) + 1;

        if (fullCount == newCount) {
            return input;
        } else {
            return new GridLine(cells.subList(0, newCount));
        }
    }

    private Map<Integer, Integer> countMap(final List<GridLine> lines) {
        Map<Integer, Integer> counts = new TreeMap<>();

        for (GridLine line : lines) {
            List<GridCell> cells = line.getCells();

            IntStream.range(0, cells.size()).forEach(i -> addToCounts(counts, i, cells.get(i)));
        }

        return counts;
    }

    private void addToCounts(final Map<Integer, Integer> counts, final int index, final GridCell cell) {
        int inc;

        if (cell.isIncluded()) {
            inc = 1;
        } else {
            inc = 0;
        }

        counts.merge(index, inc, (x, y) -> x + y);
    }
}

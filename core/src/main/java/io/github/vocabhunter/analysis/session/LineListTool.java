/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.Function.identity;

public class LineListTool<T> {
    private final List<String> lines;

    private final Map<String, Integer> indices;

    public LineListTool(final List<T> raw, final Function<T, List<String>> extractor) {
        Set<String> set = raw.stream()
            .flatMap(w -> extractor.apply(w).stream())
            .collect(Collectors.toCollection(LinkedHashSet::new));

        lines = new ArrayList<>(set);
        indices = IntStream.range(0, lines.size())
            .boxed()
            .collect(Collectors.toMap(lines::get, identity()));
    }

    public List<String> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public int getLineNo(final String line) {
        return indices.get(line);
    }
}

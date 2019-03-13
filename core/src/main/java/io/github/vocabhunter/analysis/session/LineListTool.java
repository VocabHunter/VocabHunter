/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableMap;

public class LineListTool<T> {
    private final List<String> lines;

    private final Map<String, Integer> indices;

    public LineListTool(final List<T> raw, final Function<T, List<String>> extractor) {
        lines = raw.stream()
            .flatMap(w -> extractor.apply(w).stream())
            .distinct()
            .collect(toUnmodifiableList());
        indices = IntStream.range(0, lines.size())
            .boxed()
            .collect(toUnmodifiableMap(lines::get, identity()));
    }

    public List<String> getLines() {
        return lines;
    }

    public int getLineNo(final String line) {
        return indices.get(line);
    }
}

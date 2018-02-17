/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.analysis.file.TikaTool;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class DocumentGridReaderImpl implements DocumentGridReader {
    private static final Pattern SPLITTER = Pattern.compile("\\r?\\n");

    @Override
    public List<GridLine> readGrid(final Path file, final Predicate<String> filter) {
        String fullText = TikaTool.read(file);

        if ("".equals(fullText)) {
            return Collections.emptyList();
        } else {
            return SPLITTER.splitAsStream(fullText)
                .map(String::trim)
                .map(s -> new GridCell(s, filter.test(s)))
                .map(GridLine::new)
                .collect(toList());
        }
    }
}

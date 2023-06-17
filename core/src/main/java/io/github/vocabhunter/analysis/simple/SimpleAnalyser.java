/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.simple;

import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.model.AnalysisResult;
import io.github.vocabhunter.analysis.model.WordUse;
import jakarta.inject.Singleton;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.github.vocabhunter.analysis.core.CoreConstants.LOCALE;
import static java.util.stream.Collectors.groupingBy;

@Singleton
public class SimpleAnalyser implements Analyser {
    private static final Pattern SPACE_PATTERN = Pattern.compile("[\\t\\n\\x0B\\f\\r]\\s*|\\s\\s+");

    @Override
    public AnalysisResult analyse(final String text, final String name) {
        List<String> lines = splitToList(text);
        Map<String, WordUse> map = IntStream.range(0, lines.size())
            .parallel()
            .boxed()
            .flatMap(i -> lineRecords(lines, i))
            .collect(groupingBy(AnalysisRecord::normalised, new AnalysisCollector()));
        List<WordUse> uses = map.values().stream()
            .sorted(WordStreamTool.WORD_COMPARATOR)
            .toList();

        return new AnalysisResult(name, uses, lines);
    }

    private Stream<AnalysisRecord> lineRecords(final List<String> lines, final Integer index) {
        String line = lines.get(index);

        return WordStreamTool.words(line)
            .map(w -> new AnalysisRecord(w, index));
    }

    private List<String> splitToList(final String text) {
        List<String> list = new ArrayList<>();
        BreakIterator iterator = BreakIterator.getSentenceInstance(LOCALE);
        iterator.setText(text);
        int start = iterator.first();
        int end = iterator.next();

        while (end != BreakIterator.DONE) {
            String line = text.substring(start, end).trim();

            if (!line.isEmpty()) {
                list.add(SPACE_PATTERN.matcher(line).replaceAll(" "));
            }
            start = end;
            end = iterator.next();
        }

        return list;
    }
}

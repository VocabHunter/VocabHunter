/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.simple;

import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.model.AnalysisResult;
import io.github.vocabhunter.analysis.model.WordUse;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.inject.Singleton;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Singleton
public class SimpleAnalyser implements Analyser {
    @Override
    public AnalysisResult analyse(final List<String> lines, final String name) {
        Map<String, WordUse> map = IntStream.range(0, lines.size())
            .parallel()
            .mapToObj(i -> lineRecords(lines, i))
            .flatMap(identity())
            .collect(groupingBy(AnalysisRecord::getNormalised, new AnalysisCollector()));
        List<WordUse> uses = map.values().stream()
            .sorted(WordStreamTool.WORD_COMPARATOR)
            .collect(toList());

        return new AnalysisResult(name, uses, lines);
    }

    private Stream<AnalysisRecord> lineRecords(final List<String> lines, final int index) {
        String line = lines.get(index);

        return WordStreamTool.words(line)
            .map(w -> new AnalysisRecord(w, index));
    }
}

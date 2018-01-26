/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.simple;

import io.github.vocabhunter.analysis.core.PreferredFormTool;
import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.model.AnalysisResult;
import io.github.vocabhunter.analysis.model.WordUse;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.inject.Singleton;

import static java.util.stream.Collectors.*;

@Singleton
public class SimpleAnalyser implements Analyser {
    @Override
    public AnalysisResult analyse(final List<String> lines, final String name) {
        List<AnalysisRecord> records = IntStream.range(0, lines.size())
            .boxed()
            .flatMap(i -> lineRecords(lines, i))
            .collect(toList());
        Map<String, String> identifiers = records.stream()
            .collect(toMap(AnalysisRecord::getNormalised, AnalysisRecord::getIdentifier, PreferredFormTool::preferredForm));
        Map<String, Long> counts = records.stream()
            .collect(groupingBy(AnalysisRecord::getNormalised, counting()));
        Map<String, List<Integer>> indices = records.stream()
            .collect(groupingBy(AnalysisRecord::getNormalised, mapping(AnalysisRecord::getLine, toList())));
        List<WordUse> uses = identifiers.entrySet().stream()
            .map(e -> new WordUse(e.getValue(), counts.get(e.getKey()).intValue(), indices.get(e.getKey())))
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

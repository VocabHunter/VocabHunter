/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.simple;

import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.model.AnalysisResult;
import io.github.vocabhunter.analysis.model.WordUse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.inject.Singleton;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Singleton
public class SimpleAnalyser implements Analyser {
    @Override
    public AnalysisResult analyse(final List<String> lines, final String name) {
        Map<String, WordUse> map = IntStream.range(0, lines.size())
            .boxed()
            .flatMap(n -> uses(lines, n))
            .collect(toMap(
                WordStreamTool::classifier,
                identity(),
                this::combine));
        List<WordUse> uses = map.values().stream()
                .sorted(WordStreamTool.WORD_COMPARATOR)
                .collect(toList());

        return new AnalysisResult(name, uses, lines);
    }

    private Stream<WordUse> uses(final List<String> lines, final int lineNo) {
        String line = lines.get(lineNo);
        Map<String, WordUse> map = WordStreamTool.words(line)
            .map(w -> new WordUse(w, lineNo))
            .collect(toMap(
                WordStreamTool::classifier,
                identity(),
                this::combineSingleLine));

        return map.values().stream();
    }


    private WordUse combineSingleLine(final WordUse w1, final WordUse w2) {
        return combine(w1, w2, w1.getLineNos());
    }

    private WordUse combine(final WordUse w1, final WordUse w2) {
        List<Integer> lineNos = new ArrayList<>(w1.getLineNos());

        lineNos.addAll(w2.getLineNos());

        return combine(w1, w2, lineNos);
    }

    private WordUse combine(final WordUse w1, final WordUse w2, final List<Integer> lineNos) {
        String identifier = WordStreamTool.preferredForm(w1.getWordIdentifier(), w2.getWordIdentifier());
        int useCount = w1.getUseCount() + w2.getUseCount();

        return new WordUse(identifier, useCount, lineNos);
    }
}

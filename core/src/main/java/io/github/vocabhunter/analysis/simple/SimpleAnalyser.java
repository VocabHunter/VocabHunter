/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.simple;

import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.model.AnalysisResult;
import io.github.vocabhunter.analysis.model.WordUse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class SimpleAnalyser implements Analyser {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleAnalyser.class);

    @Override
    public AnalysisResult analyse(final Stream<String> lines, final String name) {
        Map<String, WordUse> map = lines.flatMap(this::uses)
                .collect(
                        groupingBy(
                                WordStreamTool::classifier,
                                collectingAndThen(reducing(this::combine), Optional::get)));
        List<WordUse> uses = map.values().stream()
                .sorted(WordStreamTool.WORD_COMPARATOR)
                .collect(toList());
        AnalysisResult result = new AnalysisResult(name, uses);

        LOG.info("Analysed text and found {} words", uses.size());

        return result;
    }

    private Stream<WordUse> uses(final String line) {
        Map<String, WordUse> map = WordStreamTool.words(line)
            .map(w -> new WordUse(w, line))
            .collect(
                groupingBy(
                    WordStreamTool::classifier,
                    collectingAndThen(reducing(this::combineSingleLine), Optional::get)));

        return map.values().stream();
    }


    private WordUse combineSingleLine(final WordUse w1, final WordUse w2) {
        return combine(w1, w2, w1.getUses());
    }

    private WordUse combine(final WordUse w1, final WordUse w2) {
        List<String> uses = new ArrayList<>(w1.getUses());

        uses.addAll(w2.getUses());

        return combine(w1, w2, uses);
    }

    private WordUse combine(final WordUse w1, final WordUse w2, final List<String> uses) {
        String identifier = WordStreamTool.preferredForm(w1.getWordIdentifier(), w2.getWordIdentifier());
        int useCount = w1.getUseCount() + w2.getUseCount();

        return new WordUse(identifier, useCount, uses);
    }
}

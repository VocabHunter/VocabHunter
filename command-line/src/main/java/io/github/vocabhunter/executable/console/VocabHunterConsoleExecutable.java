/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.executable.console;

import com.beust.jcommander.JCommander;
import io.github.vocabhunter.analysis.file.FileStreamer;
import io.github.vocabhunter.analysis.filter.FilterBuilder;
import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.analysis.model.AnalysisResult;
import io.github.vocabhunter.analysis.model.WordUse;
import io.github.vocabhunter.analysis.session.SessionWordsTool;
import io.github.vocabhunter.analysis.simple.SimpleAnalyser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

public final class VocabHunterConsoleExecutable {
    private static final Logger LOG = LoggerFactory.getLogger(VocabHunterConsoleExecutable.class);

    private VocabHunterConsoleExecutable() {
        // Prevent instantiation - all methods are static
    }

    public static void main(final String... args) {
        try {
            VocabHunterConsoleArguments bean = new VocabHunterConsoleArguments();

            new JCommander(bean, args);

            Path file = Paths.get(bean.getInput());
            SimpleAnalyser analyser = new SimpleAnalyser();
            FileStreamer streamer = new FileStreamer(analyser);
            WordFilter wordFilter = buildFilter(bean);
            AnalysisResult model = streamer.analyse(file);

            model.getOrderedUses().stream()
                    .filter(wordFilter::isShown)
                    .forEach(w -> display(w, bean.isHideUses()));
        } catch (final Exception e) {
            LOG.error("Application error", e);
        }
    }

    private static WordFilter buildFilter(final VocabHunterConsoleArguments bean) {
        FilterBuilder builder = new FilterBuilder()
            .minimumLetters(bean.getMinLetters())
            .minimumOccurrences(bean.getMinOccurrences());

        if (bean.isIgnoreInitialCapitals()) {
            builder.excludeInitialCapital();
        }
        addFilteredWords(builder, bean.getFilterKnown(), SessionWordsTool::knownWords);
        addFilteredWords(builder, bean.getFilterSeen(), SessionWordsTool::seenWords);

        return builder.build();
    }

    private static void addFilteredWords(final FilterBuilder builder, final List<Path> filenames, final Function<Path, List<String>> extractor) {
        filenames.stream()
            .map(extractor)
            .forEach(builder::addExcludedWords);
    }

    private static void display(final WordUse use, final boolean isHideUses) {
        if (isHideUses) {
            LOG.info("{} ({})", use.getWordIdentifier(), use.getUseCount());
        } else {
            LOG.info("\n{} ({}):", use.getWordIdentifier(), use.getUseCount());
            use.getUses().stream()
                    .forEach(s -> LOG.info(" - {}", s));
        }
    }
}

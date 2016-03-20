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
import io.github.vocabhunter.analysis.simple.SimpleAnalyser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

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
                    .forEach(VocabHunterConsoleExecutable::display);
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

        return builder.build();
    }

    private static void display(final WordUse use) {
        LOG.info("\n{} ({}):", use.getWordIdentifier(), use.getUseCount());
        use.getUses().stream()
                .forEach(s -> LOG.info(" - {}", s));
    }
}

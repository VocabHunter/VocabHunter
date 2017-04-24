/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.executable.console;

import com.beust.jcommander.JCommander;
import io.github.vocabhunter.analysis.core.CoreConstants;
import io.github.vocabhunter.analysis.file.FileStreamer;
import io.github.vocabhunter.analysis.filter.FilterBuilder;
import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.analysis.model.AnalysisResult;
import io.github.vocabhunter.analysis.model.WordUse;
import io.github.vocabhunter.analysis.session.SessionWordsTool;
import io.github.vocabhunter.analysis.session.SessionWordsToolImpl;
import io.github.vocabhunter.analysis.simple.SimpleAnalyser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;

public final class VocabHunterConsoleExecutable {
    private static final Logger LOG = LoggerFactory.getLogger(VocabHunterConsoleExecutable.class);

    private static final int HELP_BUFFER_SIZE = 100;

    private VocabHunterConsoleExecutable() {
        // Prevent instantiation - all methods are static
    }

    public static void main(final String... args) {
        try {
            Instant start = Instant.now();
            VocabHunterConsoleArguments bean = new VocabHunterConsoleArguments();
            JCommander jCommander = JCommander.newBuilder()
                .addObject(bean)
                .build();

            jCommander.parse(args);
            if (bean.isHelpRequested()) {
                StringBuilder buffer = new StringBuilder(HELP_BUFFER_SIZE);

                jCommander.usage(buffer);
                LOG.info("{}", buffer);
            } else {
                String output = bean.getOutput();

                if (output == null) {
                    processInput(bean, new PrintWriter(new OutputStreamWriter(System.out, CoreConstants.CHARSET), true));
                } else {
                    try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(Paths.get(output)))) {
                        processInput(bean, out);
                    }
                }

                Instant end = Instant.now();
                Duration duration = Duration.between(start, end);
                LOG.info("\nExecution time: {}ms", duration.toMillis());
            }
        } catch (final Exception e) {
            LOG.error("Application error", e);
            LOG.error("Use -help to show the command-line options");
        }
    }

    private static void processInput(final VocabHunterConsoleArguments bean, final PrintWriter out) {
        for (String input : bean.getInput()) {
            Path file = Paths.get(input);
            SimpleAnalyser analyser = new SimpleAnalyser();
            FileStreamer streamer = new FileStreamer(analyser);
            WordFilter wordFilter = buildFilter(bean);
            AnalysisResult model = streamer.analyse(file);

            model.getOrderedUses().stream()
                    .filter(wordFilter::isShown)
                    .forEach(w -> display(out, model.getLines(), w, bean.isHideUses()));
        }
    }

    private static WordFilter buildFilter(final VocabHunterConsoleArguments bean) {
        SessionWordsTool sessionWordsTool = new SessionWordsToolImpl();
        FilterBuilder builder = new FilterBuilder()
            .minimumLetters(bean.getMinLetters())
            .minimumOccurrences(bean.getMinOccurrences());

        if (bean.isIgnoreInitialCapitals()) {
            builder.excludeInitialCapital();
        }
        addFilteredWords(builder, bean.getFilterKnown(), sessionWordsTool::knownWords);
        addFilteredWords(builder, bean.getFilterSeen(), sessionWordsTool::seenWords);

        return builder.build();
    }

    private static void addFilteredWords(final FilterBuilder builder, final List<Path> filenames, final Function<Path, List<String>> extractor) {
        filenames.stream()
            .map(extractor)
            .forEach(builder::addExcludedWords);
    }

    private static void display(final PrintWriter out, final List<String> lines, final WordUse use, final boolean isHideUses) {
        if (isHideUses) {
            out.printf("%s (%s)%n", use.getWordIdentifier(), use.getUseCount());
        } else {
            out.printf("%n%s (%s):%n", use.getWordIdentifier(), use.getUseCount());
            use.getLineNos()
                .forEach(n -> out.printf(" - %s%n", lines.get(n)));
        }
    }
}

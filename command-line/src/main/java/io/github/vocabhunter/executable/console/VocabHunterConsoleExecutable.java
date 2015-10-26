/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.executable.console;

import com.beust.jcommander.JCommander;
import io.github.vocabhunter.analysis.model.AnalysisResult;
import io.github.vocabhunter.analysis.model.WordUse;
import io.github.vocabhunter.analysis.simple.SimpleAnalyser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.util.stream.Stream;

public final class VocabHunterConsoleExecutable {
    private static final Logger LOG = LoggerFactory.getLogger(VocabHunterConsoleExecutable.class);

    private VocabHunterConsoleExecutable() {
        // Prevent instantiation - all methods are static
    }

    public static void main(final String... args) {
        try {
            VocabHunterConsoleArguments bean = new VocabHunterConsoleArguments();

            new JCommander(bean, args);

            try (Stream<String> lines = Files.lines(bean.getInput())) {
                SimpleAnalyser analyser = new SimpleAnalyser();
                AnalysisResult model = analyser.analyse(
                        lines, bean.getInput().toString(), bean.getMinLetters(), bean.getMaxWords());
                model.getOrderedUses().stream()
                        .forEach(VocabHunterConsoleExecutable::display);
            }
        } catch (final Exception e) {
            LOG.error("Application error", e);
        }
    }

    private static void display(final WordUse use) {
        LOG.info("{} ({}):", use.getWordIdentifier(), use.getUseCount());
        use.getUses().stream()
                .forEach(LOG::info);
    }
}

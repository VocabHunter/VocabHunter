/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import io.github.vocabhunter.analysis.core.CoreTool;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.model.AnalysisWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class ExcludedWordsFilter implements WordFilter {
    private static final Logger LOG = LoggerFactory.getLogger(ExcludedWordsFilter.class);

    private final List<CompletableFuture<Collection<String>>> futures;

    private final AtomicReference<Set<String>> computedExclusions = new AtomicReference<>();

    public ExcludedWordsFilter(final Executor executor, final List<Supplier<Collection<String>>> excludedWordsSuppliers) {
        futures = excludedWordsSuppliers.stream()
            .map(s -> CompletableFuture.supplyAsync(s, executor))
            .collect(toList());
    }

    @Override
    public boolean isShown(final AnalysisWord word) {
        Set<String> exclusions = exclusions();
        String identifier = CoreTool.toLowerCase(word.getWordIdentifier());

        return !exclusions.contains(identifier);
    }

    private Set<String> exclusions() {
        Set<String> result = computedExclusions.get();

        if (result == null) {
            result = buildExclusionSet();
            computedExclusions.compareAndSet(null, result);

            return computedExclusions.get();
        } else {
            return result;
        }
    }

    private Set<String> buildExclusionSet() {
        Instant start = Instant.now();
        Set<String> result = futures.stream()
            .map(this::waitForResults)
            .flatMap(Collection::stream)
            .map(CoreTool::toLowerCase)
            .collect(toSet());
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);

        LOG.info("Foreground filter list completed in {}ms", duration.toMillis());

        return result;
    }

    private Collection<String> waitForResults(final CompletableFuture<Collection<String>> future) {
        try {
            return future.join();
        } catch (final CompletionException e) {
            Throwable cause = e.getCause();

            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                throw new VocabHunterException("Failed to load filter", e);
            }
        }
    }
}

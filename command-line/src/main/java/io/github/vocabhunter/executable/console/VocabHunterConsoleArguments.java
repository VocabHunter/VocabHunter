/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.executable.console;

import com.beust.jcommander.Parameter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public class VocabHunterConsoleArguments {
    @Parameter(names = "-input", description = "Text input file", required = true)
    private String input;

    @Parameter(names = "-minletters", description = "Minimum number of letters in word for it to be shown")
    private int minLetters = 5;

    @Parameter(names = "-minoccurrences", description = "Minimum number of occurrences of word for it to be shown")
    private int minOccurrences = 3;

    @Parameter(names = "-ignoreinitialcapitals", description = "Ignore words that begin with a capital letter")
    private boolean isIgnoreInitialCapitals = false;

    @Parameter(names = "-hideuses", description = "Hide the lists of uses of each word")
    private boolean isHideUses = false;

    @Parameter(names = "-filterknown", description = "Words marked as known are filtered from these session files")
    private List<Path> filterKnown = emptyList();

    @Parameter(names = "-filterseen", description = "Words marked as known or unknown are filtered from these session files")
    private List<Path> filterSeen = emptyList();

    public String getInput() {
        return input;
    }

    public int getMinLetters() {
        return minLetters;
    }

    public int getMinOccurrences() {
        return minOccurrences;
    }

    public void setInput(final String input) {
        this.input = input;
    }

    public void setMinLetters(final int minLetters) {
        this.minLetters = minLetters;
    }

    public void setMinOccurrences(final int minOccurrences) {
        this.minOccurrences = minOccurrences;
    }

    public boolean isIgnoreInitialCapitals() {
        return isIgnoreInitialCapitals;
    }

    public void setIgnoreInitialCapitals(final boolean ignoreInitialCapitals) {
        isIgnoreInitialCapitals = ignoreInitialCapitals;
    }

    public boolean isHideUses() {
        return isHideUses;
    }

    public void setHideUses(final boolean hideUses) {
        isHideUses = hideUses;
    }

    public List<Path> getFilterKnown() {
        return unmodifiableList(filterKnown);
    }

    public void setFilterKnown(final List<Path> filterKnown) {
        this.filterKnown = new ArrayList<>(filterKnown);
    }

    public List<Path> getFilterSeen() {
        return unmodifiableList(filterSeen);
    }

    public void setFilterSeen(final List<Path> filterSeen) {
        this.filterSeen = new ArrayList<>(filterSeen);
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.executable.console;

import com.beust.jcommander.Parameter;

import java.nio.file.Path;

public class VocabHunterConsoleArguments {
    @Parameter(names = "-input", description = "Text input file", required = true)
    private Path input;

    @Parameter(names = "-minletters", description = "Minimum number of letters for word to be analysed")
    private int minLetters = 5;

    @Parameter(names = "-maxwords", description = "Maximum number of words to output")
    private int maxWords = 1000;

    public Path getInput() {
        return input;
    }

    public int getMinLetters() {
        return minLetters;
    }

    public int getMaxWords() {
        return maxWords;
    }

    public void setInput(final Path input) {
        this.input = input;
    }

    public void setMinLetters(final int minLetters) {
        this.minLetters = minLetters;
    }

    public void setMaxWords(final int maxWords) {
        this.maxWords = maxWords;
    }
}

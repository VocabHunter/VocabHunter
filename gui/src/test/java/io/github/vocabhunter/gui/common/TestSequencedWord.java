/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TestSequencedWord implements SequencedWord {
    private final String wordIdentifier;

    private final int sequenceNo;

    public TestSequencedWord(final String wordIdentifier) {
        this(wordIdentifier, -1);
    }

    public TestSequencedWord(final String wordIdentifier, final int sequenceNo) {
        this.wordIdentifier = wordIdentifier;
        this.sequenceNo = sequenceNo;
    }

    @Override
    public String getWordIdentifier() {
        return wordIdentifier;
    }

    @Override
    public int getSequenceNo() {
        return sequenceNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("wordIdentifier", wordIdentifier)
            .toString();
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class WordUse implements AnalysisWord {
    private final String wordIdentifier;

    private final int useCount;

    private final List<String> uses;

    public WordUse(final String wordIdentifier, final String use) {
        this(wordIdentifier, 1, Collections.singletonList(use));
    }

    public WordUse(final String wordIdentifier, final int useCount, final List<String> uses) {
        this.wordIdentifier = wordIdentifier;
        this.useCount = useCount;
        this.uses = new ArrayList<>(uses);
    }

    @Override
    public String getWordIdentifier() {
        return wordIdentifier;
    }

    @Override
    public int getUseCount() {
        return useCount;
    }

    public List<String> getUses() {
        return Collections.unmodifiableList(uses);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WordUse wordUse = (WordUse) o;

        return new EqualsBuilder()
                .append(useCount, wordUse.useCount)
                .append(wordIdentifier, wordUse.wordIdentifier)
                .append(uses, wordUse.uses)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(wordIdentifier)
                .append(useCount)
                .append(uses)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("wordIdentifier", wordIdentifier)
                .append("useCount", useCount)
                .append("uses", uses)
                .toString();
    }
}

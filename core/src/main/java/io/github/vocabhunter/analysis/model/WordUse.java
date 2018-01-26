/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public final class WordUse implements AnalysisWord {
    private final String wordIdentifier;

    private final int useCount;

    private final List<Integer> lineNos;

    public WordUse(final String wordIdentifier, final int useCount, final Collection<Integer> lineNos) {
        this.wordIdentifier = wordIdentifier;
        this.useCount = useCount;
        this.lineNos = lineNos.stream()
            .sorted()
            .distinct()
            .collect(toList());
    }

    @Override
    public String getWordIdentifier() {
        return wordIdentifier;
    }

    @Override
    public int getUseCount() {
        return useCount;
    }

    public List<Integer> getLineNos() {
        return Collections.unmodifiableList(lineNos);
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
                .append(lineNos, wordUse.lineNos)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(wordIdentifier)
                .append(useCount)
                .append(lineNos)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("wordIdentifier", wordIdentifier)
                .append("useCount", useCount)
                .append("lineNos", lineNos)
                .toString();
    }
}

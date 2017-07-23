/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.model;

import io.github.vocabhunter.analysis.core.PreferredFormTool;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class WordUse implements AnalysisWord {
    private final String wordIdentifier;

    private final int useCount;

    private final List<Integer> lineNos;

    public WordUse(final WordUse w1, final WordUse w2, final boolean isSingleLine) {
        lineNos = lineNos(w1, w2, isSingleLine);
        this.useCount = w1.getUseCount() + w2.getUseCount();
        this.wordIdentifier = PreferredFormTool.preferredForm(w1.getWordIdentifier(), w2.getWordIdentifier());
    }

    public WordUse(final String wordIdentifier, final int lineNo) {
        this.wordIdentifier = wordIdentifier;
        this.useCount = 1;
        this.lineNos = Collections.singletonList(lineNo);
    }

    public WordUse(final String wordIdentifier, final int useCount, final List<Integer> lineNos) {
        this.wordIdentifier = wordIdentifier;
        this.useCount = useCount;
        this.lineNos = new ArrayList<>(lineNos);
    }

    private List<Integer> lineNos(final WordUse w1, final WordUse w2, final boolean isSingleLine) {
        List<Integer> lineNos1 = w1.getLineNos();

        if (isSingleLine) {
            return lineNos1;
        } else {
            List<Integer> lineNos2 = w2.getLineNos();
            List<Integer> result = new ArrayList<>(lineNos1.size() + lineNos2.size());

            result.addAll(lineNos1);
            result.addAll(lineNos2);

            return result;
        }
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

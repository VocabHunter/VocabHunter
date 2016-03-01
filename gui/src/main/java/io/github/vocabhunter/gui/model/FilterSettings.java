/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public final class FilterSettings {
    private final int minimumLetters;

    private final int minimumOccurrences;

    public FilterSettings(final int minimumLetters, final int minimumOccurrences) {
        this.minimumLetters = minimumLetters;
        this.minimumOccurrences = minimumOccurrences;
    }

    public int getMinimumLetters() {
        return minimumLetters;
    }

    public int getMinimumOccurrences() {
        return minimumOccurrences;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FilterSettings settings = (FilterSettings) o;

        return new EqualsBuilder()
            .append(minimumLetters, settings.minimumLetters)
            .append(minimumOccurrences, settings.minimumOccurrences)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(minimumLetters)
            .append(minimumOccurrences)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("minimumLetters", minimumLetters)
            .append("minimumOccurrences", minimumOccurrences)
            .toString();
    }
}

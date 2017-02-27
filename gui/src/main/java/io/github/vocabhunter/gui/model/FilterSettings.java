/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.settings.BaseListedFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class FilterSettings {
    private final int minimumLetters;

    private final int minimumOccurrences;

    private final boolean allowInitialCapitals;

    private final List<BaseListedFile> filterFiles;

    public FilterSettings(final int minimumLetters, final int minimumOccurrences, final boolean allowInitialCapitals, final List<BaseListedFile> filterFiles) {
        this.minimumLetters = minimumLetters;
        this.minimumOccurrences = minimumOccurrences;
        this.allowInitialCapitals = allowInitialCapitals;
        this.filterFiles = new ArrayList<>(filterFiles);
    }

    public int getMinimumLetters() {
        return minimumLetters;
    }

    public int getMinimumOccurrences() {
        return minimumOccurrences;
    }

    public boolean isAllowInitialCapitals() {
        return allowInitialCapitals;
    }

    public List<BaseListedFile> getFilterFiles() {
        return Collections.unmodifiableList(filterFiles);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FilterSettings that = (FilterSettings) o;

        return new EqualsBuilder()
            .append(minimumLetters, that.minimumLetters)
            .append(minimumOccurrences, that.minimumOccurrences)
            .append(allowInitialCapitals, that.allowInitialCapitals)
            .append(filterFiles, that.filterFiles)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(minimumLetters)
            .append(minimumOccurrences)
            .append(allowInitialCapitals)
            .append(filterFiles)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("minimumLetters", minimumLetters)
            .append("minimumOccurrences", minimumOccurrences)
            .append("allowInitialCapitals", allowInitialCapitals)
            .append("filterFiles", filterFiles)
            .toString();
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.simple;

import io.github.vocabhunter.analysis.core.CoreTool;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AnalysisRecord {
    private final String identifier;

    private final String normalised;

    private final int line;

    public AnalysisRecord(final String identifier, final int line) {
        this.identifier = identifier;
        this.normalised = CoreTool.toLowerCase(identifier);
        this.line = line;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getNormalised() {
        return normalised;
    }

    public int getLine() {
        return line;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnalysisRecord that = (AnalysisRecord) o;

        return new EqualsBuilder()
            .append(line, that.line)
            .append(identifier, that.identifier)
            .append(normalised, that.normalised)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(identifier)
            .append(normalised)
            .append(line)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("identifier", identifier)
            .append("normalised", normalised)
            .append("line", line)
            .toString();
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public final class ExcelListedFile extends BaseListedFile {
    private final Set<Integer> columns;

    @JsonCreator
    public ExcelListedFile(
        @JsonProperty("file")
        final Path file,
        @JsonProperty("columns")
        final Collection<Integer> columns) {
        super(file);
        this.columns = new TreeSet<>(columns);
    }

    public Set<Integer> getColumns() {
        return Collections.unmodifiableSet(columns);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExcelListedFile that = (ExcelListedFile) o;

        return new EqualsBuilder()
            .appendSuper(super.equals(o))
            .append(columns, that.columns)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .appendSuper(super.hashCode())
            .append(columns)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("file", getFile())
            .append("columns", columns)
            .toString();
    }
}

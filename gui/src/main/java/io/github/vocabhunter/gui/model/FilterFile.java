/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.nio.file.Path;

public final class FilterFile {
    private final Path file;

    private final FilterFileMode mode;

    public FilterFile(final Path file, final FilterFileMode mode) {
        this.file = file;
        this.mode = mode;
    }

    public Path getFile() {
        return file;
    }

    public FilterFileMode getMode() {
        return mode;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FilterFile that = (FilterFile) o;

        return new EqualsBuilder()
            .append(file, that.file)
            .append(mode, that.mode)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(file)
            .append(mode)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("file", file)
            .append("mode", mode)
            .toString();
    }
}

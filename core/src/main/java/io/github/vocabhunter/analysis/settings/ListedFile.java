/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.nio.file.Path;

public final class ListedFile {
    private Path file;

    private ListedFileType type;

    private boolean isIncludeUnknown;

    public ListedFile() {
        // No argument constructor to allow use as standard Java Bean
    }

    public ListedFile(final Path file, final ListedFileType type, final boolean isIncludeUnknown) {
        this.file = file;
        this.type = type;
        this.isIncludeUnknown = isIncludeUnknown;
    }

    public Path getFile() {
        return file;
    }

    public void setFile(final Path file) {
        this.file = file;
    }

    public ListedFileType getType() {
        return type;
    }

    public void setType(final ListedFileType type) {
        this.type = type;
    }

    public boolean isIncludeUnknown() {
        return isIncludeUnknown;
    }

    public void setIncludeUnknown(final boolean includeUnknown) {
        isIncludeUnknown = includeUnknown;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ListedFile that = (ListedFile) o;

        return new EqualsBuilder()
            .append(isIncludeUnknown, that.isIncludeUnknown)
            .append(file, that.file)
            .append(type, that.type)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(file)
            .append(type)
            .append(isIncludeUnknown)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("file", file)
            .append("type", type)
            .append("isIncludeUnknown", isIncludeUnknown)
            .toString();
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.nio.file.Path;

public final class FileChoice {
    private final Path file;

    private final FileFormatType type;

    public FileChoice(final Path file, final FileFormatType type) {
        this.file = file;
        this.type = type;
    }

    public Path getFile() {
        return file;
    }

    public FileFormatType getType() {
        return type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FileChoice that = (FileChoice) o;

        return new EqualsBuilder()
            .append(file, that.file)
            .append(type, that.type)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(file)
            .append(type)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("file", file)
            .append("type", type)
            .toString();
    }
}

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

public final class SessionListedFile extends BaseListedFile {
    private final boolean isIncludeUnknown;

    @JsonCreator
    public SessionListedFile(
        @JsonProperty("file")
        final Path file,
        @JsonProperty("includeUnknown")
        final boolean isIncludeUnknown) {
        super(file);
        this.isIncludeUnknown = isIncludeUnknown;
    }

    public boolean isIncludeUnknown() {
        return isIncludeUnknown;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SessionListedFile that = (SessionListedFile) o;

        return new EqualsBuilder()
            .appendSuper(super.equals(o))
            .append(isIncludeUnknown, that.isIncludeUnknown)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .appendSuper(super.hashCode())
            .append(isIncludeUnknown)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("file", getFile())
            .append("isIncludeUnknown", isIncludeUnknown)
            .toString();
    }
}

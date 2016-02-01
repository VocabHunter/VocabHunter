/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.nio.file.Path;
import java.util.Optional;

public class EnrichedSessionState {
    private final SessionState state;

    private final Path file;

    public EnrichedSessionState(final SessionState state) {
        this(state, null);
    }

    public EnrichedSessionState(final SessionState state, final Path file) {
        this.state = state;
        this.file = file;
    }

    public SessionState getState() {
        return state;
    }

    public Optional<Path> getFile() {
        return Optional.ofNullable(file);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EnrichedSessionState that = (EnrichedSessionState) o;

        return new EqualsBuilder()
            .append(state, that.state)
            .append(file, that.file)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(state)
            .append(file)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("file", file)
            .append("state", state)
            .toString();
    }
}

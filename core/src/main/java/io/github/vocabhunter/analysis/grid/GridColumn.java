/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class GridColumn {
    private final int acceptedCount;

    public GridColumn(final int acceptedCount) {
        this.acceptedCount = acceptedCount;
    }

    public int getAcceptedCount() {
        return acceptedCount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GridColumn that = (GridColumn) o;

        return new EqualsBuilder()
            .append(acceptedCount, that.acceptedCount)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(acceptedCount)
            .toHashCode();
    }
}

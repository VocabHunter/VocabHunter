/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class GridCell {
    public static final GridCell EMPTY_CELL = new GridCell("", false);

    private final String content;

    private final boolean isRejected;

    public GridCell(final String content, final boolean isRejected) {
        this.content = content;
        this.isRejected = isRejected;
    }

    public String getContent() {
        return content;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public boolean isIncluded() {
        return !(isRejected() || isEmpty());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GridCell gridCell = (GridCell) o;

        return new EqualsBuilder()
            .append(isRejected, gridCell.isRejected)
            .append(content, gridCell.content)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(content)
            .append(isRejected)
            .toHashCode();
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(content);

        if (isEmpty()) {
            buffer.append("(empty)");
        }
        if (isRejected) {
            buffer.append(" (rejected)");
        }

        return buffer.toString();
    }
}

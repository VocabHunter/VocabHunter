/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FileToolTestBean {
    private String left;

    private String right;

    public FileToolTestBean() {
        // No argument constructor to allow use as standard Java Bean
    }

    public FileToolTestBean(final String left, final String right) {
        this.left = left;
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(final String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(final String right) {
        this.right = right;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FileToolTestBean that = (FileToolTestBean) o;

        return new EqualsBuilder()
            .append(left, that.left)
            .append(right, that.right)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(left)
            .append(right)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("left", left)
            .append("right", right)
            .toString();
    }
}

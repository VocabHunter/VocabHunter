/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.model.AnalysisResult;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SessionState {
    private int formatVersion = 1;

    private String name;

    private List<SessionWord> orderedUses = Collections.emptyList();

    public SessionState() {
        // No argument constructor to allow use as standard Java Bean
    }

    public SessionState(final AnalysisResult model) {
        this.name = model.getName();

        orderedUses = model.getOrderedUses().stream()
                .map(SessionWord::new)
                .collect(Collectors.toList());
    }

    public int getFormatVersion() {
        return formatVersion;
    }

    public void setFormatVersion(final int formatVersion) {
        this.formatVersion = formatVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<SessionWord> getOrderedUses() {
        return Collections.unmodifiableList(orderedUses);
    }

    public void setOrderedUses(final List<SessionWord> orderedUses) {
        this.orderedUses = new ArrayList<>(orderedUses);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SessionState that = (SessionState) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(orderedUses, that.orderedUses)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(orderedUses)
                .toHashCode();
    }
}

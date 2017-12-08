/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.vocabhunter.analysis.model.AnalysisResult;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionState {
    private int formatVersion = SessionFormatVersion.LATEST_VERSION;

    private String name;

    private List<SessionWord> orderedUses = Collections.emptyList();

    private List<String> lines = Collections.emptyList();

    public SessionState() {
        // No argument constructor to allow use as standard Java Bean
    }

    public SessionState(final AnalysisResult model) {
        this.name = model.getName();

        orderedUses = model.getOrderedUses().stream()
                .map(SessionWord::new)
                .collect(Collectors.toList());
        lines = new ArrayList<>(model.getLines());
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

    public List<String> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public void setLines(final List<String> lines) {
        this.lines = new ArrayList<>(lines);
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
            .append(formatVersion, that.formatVersion)
            .append(name, that.name)
            .append(orderedUses, that.orderedUses)
            .append(lines, that.lines)
            .isEquals();
    }

    public boolean isEquivalent(final SessionState that) {
        return formatVersion == that.formatVersion
            && name.equals(that.name)
            && isEquivalentLines(lines, that.lines)
            && isEquivalentUses(orderedUses, that.orderedUses, this.lines, that.lines);
    }

    private static boolean isEquivalentLines(final List<String> lhs, final List<String> rhs) {
        Set<String> lhsSet = new HashSet<>(lhs);
        Set<String> rhsSet = new HashSet<>(rhs);

        return lhsSet.equals(rhsSet);
    }

    private static boolean isEquivalentUses(final List<SessionWord> lhs, final List<SessionWord> rhs, final List<String> lhsLines, final List<String> rhsLines) {
        if (lhs.size() != rhs.size()) {
            return false;
        }
        Iterator<SessionWord> lhsI = lhs.iterator();
        Iterator<SessionWord> rhsI = rhs.iterator();

        while (lhsI.hasNext()) {
            SessionWord lhsWord = lhsI.next();
            SessionWord rhsWord = rhsI.next();

            Function<SessionWord, List<?>> lhsF = extractor(lhsLines);
            Function<SessionWord, List<?>> rhsF = extractor(rhsLines);

            if (!lhsWord.isEquivalent(rhsWord, lhsF, rhsF)) {
                return false;
            }
        }

        return true;
    }

    private static Function<SessionWord, List<?>> extractor(final List<String> lhsLines) {
        return w -> w.getLineNos().stream()
                    .map(lhsLines::get)
                    .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(formatVersion)
            .append(name)
            .append(orderedUses)
            .append(lines)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("formatVersion", formatVersion)
            .append("name", name)
            .append("orderedUses", orderedUses)
            .append("lines", lines)
            .toString();
    }
}

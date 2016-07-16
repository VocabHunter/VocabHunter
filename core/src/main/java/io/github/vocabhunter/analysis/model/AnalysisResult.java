/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.model;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public final class AnalysisResult {
    private final String name;

    private final List<WordUse> orderedUses;

    private final List<String> lines;

    public AnalysisResult(final String name, final List<WordUse> orderedUses, final List<String> lines) {
        this.name = name;
        this.orderedUses = new ArrayList<>(orderedUses);
        this.lines = lines;
    }

    public String getName() {
        return name;
    }

    public List<WordUse> getOrderedUses() {
        return unmodifiableList(orderedUses);
    }

    public List<String> getLines() {
        return unmodifiableList(lines);
    }
}

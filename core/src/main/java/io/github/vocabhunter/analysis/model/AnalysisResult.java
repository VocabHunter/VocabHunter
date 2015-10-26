/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AnalysisResult {
    private final String name;

    private final List<WordUse> orderedUses;

    public AnalysisResult(final String name, final List<WordUse> orderedUses) {
        this.name = name;
        this.orderedUses = new ArrayList<>(orderedUses);
    }

    public String getName() {
        return name;
    }

    public List<WordUse> getOrderedUses() {
        return Collections.unmodifiableList(orderedUses);
    }
}

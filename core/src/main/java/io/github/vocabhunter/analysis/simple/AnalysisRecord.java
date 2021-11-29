/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.simple;

import io.github.vocabhunter.analysis.core.CoreTool;

public record AnalysisRecord(String identifier, String normalised, Integer line) {
    public AnalysisRecord(final String identifier, final Integer line) {
        this(identifier, CoreTool.toLowerCase(identifier), line);
    }
}

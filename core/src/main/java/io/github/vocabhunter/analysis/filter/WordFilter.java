/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import io.github.vocabhunter.analysis.model.AnalysisWord;

@FunctionalInterface
public interface WordFilter {
    boolean isShown(AnalysisWord word);
}

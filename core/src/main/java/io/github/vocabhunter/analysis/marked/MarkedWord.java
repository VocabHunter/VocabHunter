/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.marked;

import io.github.vocabhunter.analysis.model.AnalysisWord;

public interface MarkedWord extends AnalysisWord {
    WordState getState();
}

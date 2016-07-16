/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.model;

import java.util.List;

public interface Analyser {
    AnalysisResult analyse(List<String> lines, String name);
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.model;

import java.util.stream.Stream;

public interface Analyser {
    AnalysisResult analyse(Stream<String> lines, String name);
}

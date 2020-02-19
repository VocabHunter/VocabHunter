/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.model;

public interface Analyser {
    AnalysisResult analyse(String text, String name);
}

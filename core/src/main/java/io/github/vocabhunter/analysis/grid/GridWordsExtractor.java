/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import java.util.List;
import java.util.Set;

public interface GridWordsExtractor {
    List<String> words(List<GridLine> lines, Set<Integer> columns);
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import java.util.List;

@FunctionalInterface
public interface TextGridBuilder {
    TextGrid build(final List<GridLine> lines);
}

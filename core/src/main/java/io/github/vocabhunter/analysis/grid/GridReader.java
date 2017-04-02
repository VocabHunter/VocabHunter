/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

public interface GridReader {
    List<GridLine> readGrid(Path file, Predicate<String> filter);
}

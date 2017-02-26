/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import java.nio.file.Path;

public interface TextGridManager {
    TextGrid readDocument(Path file);

    TextGrid readExcel(Path file);
}

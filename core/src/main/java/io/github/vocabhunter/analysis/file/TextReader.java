/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.file;

import java.nio.file.Path;

public interface TextReader {
    String read(Path file);
}

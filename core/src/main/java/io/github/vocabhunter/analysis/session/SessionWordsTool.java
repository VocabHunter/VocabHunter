/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import java.nio.file.Path;
import java.util.List;

public interface SessionWordsTool {
    List<String> knownWords(Path file);

    List<String> seenWords(Path file);

    List<SessionWord> readMarkedWords(Path file);
}

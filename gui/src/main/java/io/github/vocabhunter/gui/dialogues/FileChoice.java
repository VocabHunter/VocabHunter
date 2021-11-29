/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import java.nio.file.Path;

public record FileChoice(Path file, FileFormatType type) {
    // No additional record configuration required
}

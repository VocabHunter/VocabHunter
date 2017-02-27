/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.analysis.settings.BaseListedFile;

import java.util.List;

public interface FilterFileWordsExtractor {
    List<String> extract(BaseListedFile file);
}

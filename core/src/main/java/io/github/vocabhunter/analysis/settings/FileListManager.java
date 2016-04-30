/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import java.util.List;

public interface FileListManager {
    List<ListedFile> getFilteredSessionFiles();

    void setFilteredSessionFiles(List<ListedFile> files);
}

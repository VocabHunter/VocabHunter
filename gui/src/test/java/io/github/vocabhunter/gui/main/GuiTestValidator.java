/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

public interface GuiTestValidator {
    void validateWebPage(final String page);

    void validateSavedSession(final String name);

    void validateExportFile();
}

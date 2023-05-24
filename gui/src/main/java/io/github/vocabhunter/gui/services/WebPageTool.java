/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.gui.i18n.I18nKey;

public interface WebPageTool {
    void showWebPage(String page);

    void showWebPage(I18nKey key);
}

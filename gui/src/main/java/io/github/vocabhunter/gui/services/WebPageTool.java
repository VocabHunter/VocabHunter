/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.gui.i18n.I18nKey;

public interface WebPageTool {
    void showWebPage(final String page);

    void showWebPage(final I18nKey key);
}

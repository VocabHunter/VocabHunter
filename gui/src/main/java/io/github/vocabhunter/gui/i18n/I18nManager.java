/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.i18n;

import io.github.vocabhunter.analysis.core.I18nBundleProvider;

public interface I18nManager extends I18nBundleProvider {
    void initialise();

    String text(I18nKey key, Object... arguments);
}

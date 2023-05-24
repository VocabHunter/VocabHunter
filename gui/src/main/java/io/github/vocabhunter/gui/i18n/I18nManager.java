/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.i18n;

import io.github.vocabhunter.analysis.core.I18nBundleProvider;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ObservableValue;

public interface I18nManager extends I18nBundleProvider {
    void setupLocale(SupportedLocale locale);

    String text(I18nKey key, Object... arguments);

    String text(SupportedLocale locale, I18nKey key, Object... arguments);

    StringExpression textBinding(I18nKey key, ObservableValue<?>... arguments);
}

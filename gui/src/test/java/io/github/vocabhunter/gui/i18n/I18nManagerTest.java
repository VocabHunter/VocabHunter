/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.i18n;

import io.github.vocabhunter.analysis.core.CoreConstants;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static io.github.vocabhunter.gui.i18n.I18nKey.SESSION_TAB_ANALYSIS;
import static io.github.vocabhunter.gui.i18n.I18nKey.STATUS_MARKED_PERCENTAGE;
import static io.github.vocabhunter.gui.i18n.SupportedLocale.SPANISH;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class I18nManagerTest {
    private final I18nManager target = I18nManagerImpl.createForDefaultLocale();

    @Test
    public void testGuiBundle() {
        String value = target.bundle().getString(SESSION_TAB_ANALYSIS.getKey());

        validateGuiValue(value);
    }

    @Test
    public void testGuiText() {
        validateDefaultLocaleValue();
    }

    @Test
    public void testZeroPercent() {
        validatePercentage("0% of words marked", 0);
    }

    @Test
    public void testOneHundredPercent() {
        validatePercentage("100% of words marked", 100);
    }

    @Test
    public void testRoundUpPercent() {
        validatePercentage("7% of words marked", 7.4);
    }

    @Test
    public void testRoundDownPercent() {
        validatePercentage("8% of words marked", 7.6);
    }

    @Test
    public void testDefaultLocale() {
        assertAll(
            () -> validateDefaultLocale(CoreConstants.LOCALE),
            this::validateDefaultLocaleValue
        );
    }

    @Test
    public void testSpanishLocale() {
        target.setupLocale(SPANISH);
        assertAll(
            () -> validateDefaultLocale(SPANISH.getLocale()),
            () -> assertEquals("Analysis De Texto", target.text(SESSION_TAB_ANALYSIS))
        );
    }

    private void validatePercentage(final String expected, final double value) {
        SimpleDoubleProperty property = new SimpleDoubleProperty(value);

        StringExpression result = target.textBinding(STATUS_MARKED_PERCENTAGE, property);

        assertEquals(expected, result.getValue());
    }

    private void validateDefaultLocaleValue() {
        String value = target.text(SESSION_TAB_ANALYSIS);

        validateGuiValue(value);
    }

    private void validateGuiValue(final String value) {
        assertEquals("Text Analysis", value);
    }

    private void validateDefaultLocale(final Locale expected) {
        assertEquals(expected, Locale.getDefault());
    }
}

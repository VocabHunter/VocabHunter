/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.i18n;

import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.jupiter.api.Test;

import static io.github.vocabhunter.gui.i18n.I18nKey.MAIN_WINDOW_UNTITLED;
import static io.github.vocabhunter.gui.i18n.I18nKey.STATUS_MARKED_PERCENTAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class I18nManagerTest {
    private final I18nManager target = new I18nManagerImpl();

    @Test
    public void testGuiBundle() {
        String value = target.bundle().getString(MAIN_WINDOW_UNTITLED.getKey());

        validateGuiValue(value);
    }

    @Test
    public void testGuiText() {
        String value = target.text(MAIN_WINDOW_UNTITLED);

        validateGuiValue(value);
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

    private void validatePercentage(final String expected, final double value) {
        SimpleDoubleProperty property = new SimpleDoubleProperty(value);

        StringExpression result = target.textBinding(STATUS_MARKED_PERCENTAGE, property);

        assertEquals(expected, result.getValue());
    }

    private void validateGuiValue(final String value) {
        assertEquals("Untitled", value);
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.i18n;

import org.junit.jupiter.api.Test;

import static io.github.vocabhunter.gui.i18n.I18nKey.MAIN_WINDOW_UNTITLED;
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

    private void validateGuiValue(final String value) {
        assertEquals("Untitled", value);
    }
}

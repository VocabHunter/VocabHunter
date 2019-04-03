/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.i18n.I18nManagerImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColumnNameToolTest {
    private final I18nManager i18nManager = I18nManagerImpl.createForDefaultLocale();

    private final ColumnNameTool target = new ColumnNameTool(i18nManager);

    @ParameterizedTest
    @CsvSource({
        "0,    Column A",
        "1,    Column B",
        "25,   Column Z",
        "26,   Column AA",
        "27,   Column AB",
        "701,  Column ZZ",
        "702,  Column AAA",
        "1023, Column AMJ",
    })
    public void testColumn(final int index, final String expected) {
        String actual = target.columnName(index);

        assertEquals(expected, actual);
    }
}

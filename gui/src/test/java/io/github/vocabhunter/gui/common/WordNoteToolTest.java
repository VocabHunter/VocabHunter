/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WordNoteToolTest {
    @ParameterizedTest
    @CsvSource({
        // Empties and nulls
        ",               ,                           false, ''",
        ",               '',                         false, ''",
        "'',             ,                           false, ''",
        "'',             '',                         false, ''",
        "'',             ' ',                        false, ''",

        // Single line text
        "'simple',       'simple',                   false, 'simple'",
        "'simple',       ' simple ',                 false, 'simple'",
        "'original',     'changed',                  true,  'changed'",
        "'original',     ' changed ',                true,  'changed'",

        // Multi-line text
        "'Line1\nLine2', 'Line1\nLine2',             false, 'Line1\nLine2'",
        "'Line1\nLine2', '\nLine1\nLine2\n',         false, 'Line1\nLine2'",
        "'Line1\nLine2', 'Line1\nModifiedLine2',     true,  'Line1\nModifiedLine2'",
        "'Line1\nLine2', '\nLine1\nModifiedLine2\n', true,  'Line1\nModifiedLine2'"
    })
    public void testWordNoteTool(final String original, final String latest, final boolean isModified, final String cleaned) {
        WordNoteTool tool = new WordNoteTool(original, latest);

        assertAll(
            () -> assertEquals(cleaned, tool.getCleaned()),
            () -> assertEquals(isModified, tool.isModified())
        );
    }
}

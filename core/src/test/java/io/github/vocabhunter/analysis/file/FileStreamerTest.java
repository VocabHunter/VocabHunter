/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.file;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileStreamerTest {
    private static final List<String> LINES = Arrays.asList(
            "The quick brown fox jumped over the lazy dog's back.",
            "Now is the time for all good men to come to the aid of the party.",
            "This is a simple test document.");

    private static final String FILE_EMPTY = "empty.txt";

    private static final String FILE_TEXT = "sample.txt";

    private static final String FILE_WORD = "sample.doc";

    private static final String FILE_OPEN_OFFICE = "sample.odt";

    private static final String FILE_PDF = "sample.pdf";

    @Test(expected = VocabHunterException.class)
    public void testEmpty() throws Exception {
        validate(FILE_EMPTY);
    }

    @Test
    public void testText() throws Exception {
        validate(FILE_TEXT);
    }

    @Test
    public void testWord() throws Exception {
        validate(FILE_WORD);
    }

    @Test
    public void testOpenOffice() throws Exception {
        validate(FILE_OPEN_OFFICE);
    }

    @Test
    public void testPdf() throws Exception {
        validate(FILE_PDF);
    }

    private void validate(final String file) {
        InputStream in = FileStreamerTest.class.getResourceAsStream("/" + file);
        List<String> result = FileStreamer.stream(in, Paths.get(file))
                .collect(Collectors.toList());

        Assert.assertEquals(LINES, result);
    }
}
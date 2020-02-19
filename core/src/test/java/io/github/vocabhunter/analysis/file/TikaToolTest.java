/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.file;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TikaToolTest {
    private static final Pattern SPACE_PATTERN = Pattern.compile("\\s+");

    private static final String TEXT =
        "The quick brown fox jumped over the lazy dog's back. "
        + "Now is the time for all good men to come to the aid of the party. "
        + "This is a simple test document.";

    private final TextReader target = new TikaTool();

    @ParameterizedTest
    @ValueSource(strings = {"sample.txt", "sample.doc", "sample.odt", "sample.pdf"})
    public void testRead(final String file) throws Exception {
        String raw = target.read(getFile(file));
        String result = SPACE_PATTERN.matcher(raw).replaceAll(" ").trim();

        assertEquals(TEXT, result, "Lines from file");
    }

    private Path getFile(final String fileName) throws Exception {
        URL resource = TikaToolTest.class.getResource("/" + fileName);

        return Paths.get(resource.toURI());
    }
}

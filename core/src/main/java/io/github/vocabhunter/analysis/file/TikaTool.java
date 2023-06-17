/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.file;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import jakarta.inject.Singleton;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.exception.ZeroByteFileException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static io.github.vocabhunter.analysis.session.FileNameTool.filename;

@Singleton
public class TikaTool implements TextReader {
    private static final Logger LOG = LoggerFactory.getLogger(TikaTool.class);

    @Override
    public String read(final Path file) {
        Metadata metadata = new Metadata();

        try (InputStream in = TikaInputStream.get(file, metadata)) {
            Tika tika = new Tika();

            return tika.parseToString(in, metadata, -1);
        } catch (ZeroByteFileException e) {
            LOG.debug("Empty file", e);

            return "";
        } catch (IOException | TikaException e) {
            throw new VocabHunterException(String.format("Unable to read file '%s'", filename(file)), e);
        }
    }
}

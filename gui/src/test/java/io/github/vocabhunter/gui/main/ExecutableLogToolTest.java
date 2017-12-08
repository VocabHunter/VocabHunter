/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExecutableLogToolTest {
    private static final Logger TARGET_LOG = (Logger) LoggerFactory.getLogger(ExecutableLogTool.class);

    private final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

    private final AppenderForTesting appender = new AppenderForTesting();

    @BeforeEach
    public void setUp() {
        TARGET_LOG.addAppender(appender);
        TARGET_LOG.setLevel(Level.INFO);
        TARGET_LOG.setAdditive(true);
        appender.setContext(loggerContext);
        appender.start();
    }

    @AfterEach
    public void tearDown() {
        appender.stop();
        TARGET_LOG.detachAppender(appender);
    }

    @Test
    public void testLogSystemDetails() {
        validate(ExecutableLogTool::logSystemDetails);
    }

    @Test
    public void testLogShutdown() {
        validate(ExecutableLogTool::logShutdown);
    }

    @Test
    public void testLogStartup() {
        validate(ExecutableLogTool::logStartup);
    }

    @Test
    public void testLogError() {
        validate(() -> ExecutableLogTool.logError(new VocabHunterException("Test")));
    }

    private void validate(final Runnable r) {
        r.run();

        String messages = appender.getMessages().stream()
            .collect(joining());
        assertTrue(StringUtils.isNotBlank(messages));
    }
}

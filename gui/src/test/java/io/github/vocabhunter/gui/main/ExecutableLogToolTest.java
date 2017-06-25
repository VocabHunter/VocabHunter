/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertTrue;

public class ExecutableLogToolTest {
    private static final Logger TARGET_LOG = (Logger) LoggerFactory.getLogger(ExecutableLogTool.class);

    private final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

    private final AppenderForTesting appender = new AppenderForTesting();

    @Before
    public void setUp() {
        TARGET_LOG.addAppender(appender);
        TARGET_LOG.setLevel(Level.INFO);
        TARGET_LOG.setAdditive(true);
        appender.setContext(loggerContext);
        appender.start();
    }

    @After
    public void tearDown() {
        appender.stop();
        TARGET_LOG.detachAppender(appender);
    }

    @Test
    public void testLogSystemDetails() throws Exception {
        validate(ExecutableLogTool::logSystemDetails);
    }

    @Test
    public void testLogShutdown() throws Exception {
        validate(ExecutableLogTool::logShutdown);
    }

    @Test
    public void testLogStartup() throws Exception {
        validate(ExecutableLogTool::logStartup);
    }

    @Test
    public void testLogError() throws Exception {
        validate(() -> ExecutableLogTool.logError(new VocabHunterException("Test")));
    }

    private void validate(final Runnable r) {
        r.run();

        String messages = appender.getMessages().stream()
            .collect(joining());
        assertTrue(StringUtils.isNotBlank(messages));
    }
}

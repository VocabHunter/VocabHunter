/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.gui.common.BuildInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.stream.Stream;

public final class ExecutableLogTool {
    private static final double BYTES_TO_MEGS = 1024d * 1024d;

    private static final Logger LOG = LoggerFactory.getLogger(ExecutableLogTool.class);

    private ExecutableLogTool() {
        // Prevent instantiation - all methods are static
    }

    public static void logSystemDetails() {
        logSystemProperties("Java: {} ({}) - OS: {} ({}, {})", "java.version", "java.vendor", "os.name", "os.version", "os.arch");
        logRuntimeDetails();
    }

    private static void logSystemProperties(final String template, final String... keys) {
        Object[] args = Stream.of(keys)
                .map(System::getProperty)
                .toArray();

        LOG.info(template, args);
    }

    private static void logRuntimeDetails() {
        int cores = Runtime.getRuntime().availableProcessors();
        String freeMemory = memory(Runtime.getRuntime().freeMemory());
        String maxMemory = memory(Runtime.getRuntime().maxMemory());
        String totalMemory = memory(Runtime.getRuntime().totalMemory());

        LOG.info("Cores: {} - Memory: ({} free, {} max, {} total)", cores, freeMemory, maxMemory, totalMemory);
    }

    private static String memory(final long bytes) {
        if (bytes == Long.MAX_VALUE) {
            return "no limit";
        } else {
            double value = bytes / BYTES_TO_MEGS;

            return new DecimalFormat("#.0M").format(value);
        }
    }

    public static void logShutdown() {
        LOG.info("Application shutdown");
    }

    public static void logStartup() {
        LOG.info("Application startup");
        LOG.info("VocabHunter version: {}", BuildInfo.version());
    }

    public static void logError(final Throwable e) {
        LOG.error("Application error", e);
    }
}

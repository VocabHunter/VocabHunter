/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class BuildInfo {
    private static final Logger LOG = LoggerFactory.getLogger(BuildInfo.class);

    private static final String PROPERTIES_FILE = "properties/build-info.properties";

    private BuildInfo() {
        // Prevent instantiation - all methods are static
    }

    public static String version() {
        try {
            try (InputStream in = BuildInfo.class.getResourceAsStream("/" + PROPERTIES_FILE)) {
                if (in != null) {
                    Properties p = new Properties();
                    p.load(in);
                    String version = p.getProperty("version");

                    if (StringUtils.isNotBlank(version)) {
                        return version;
                    }
                }
            }
        } catch (final IOException e) {
            LOG.error("Unable to read file {}", PROPERTIES_FILE, e);
        }

        return "Undefined";
    }
}


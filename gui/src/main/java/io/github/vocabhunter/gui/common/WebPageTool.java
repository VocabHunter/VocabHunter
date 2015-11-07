/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import io.github.vocabhunter.analysis.core.VocabHunterException;

import java.awt.*;
import java.net.URI;

public final class WebPageTool {
    private WebPageTool() {
        // Prevent instantiation - all methods are static
    }

    public static void showWebPage(final String page) {
        try {
            Desktop.getDesktop().browse(new URI(page));
        } catch (final Exception e) {
            throw new VocabHunterException(String.format("Unable to open page %s", page), e);
        }
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import static io.github.vocabhunter.gui.common.GuiConstants.FONT_AWESOME;

public final class IconTool {
    private static final String STYLE_CLASS = "fontAwesomeIcon";

    private IconTool() {
        // Prevent instantiation - all methods are static
    }

    public static Glyph icon(final FontAwesome.Glyph icon) {
        Glyph glyph = new Glyph(FONT_AWESOME, icon);

        glyph.getStyleClass().add(STYLE_CLASS);

        return glyph;
    }
}

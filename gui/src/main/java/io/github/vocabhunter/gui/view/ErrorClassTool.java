/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import javafx.css.PseudoClass;
import javafx.scene.Node;

public final class ErrorClassTool {
    private static final PseudoClass CLASS_FAIL = PseudoClass.getPseudoClass("fail");

    private ErrorClassTool() {
        // Prevent instantiation - all methods are private
    }

    public static void updateClass(final Node node, final boolean isFail) {
        node.pseudoClassStateChanged(CLASS_FAIL, isFail);
    }
}

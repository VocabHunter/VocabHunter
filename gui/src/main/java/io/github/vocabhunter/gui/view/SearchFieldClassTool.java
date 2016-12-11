/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import javafx.css.PseudoClass;
import javafx.scene.Node;

public final class SearchFieldClassTool {
    private static final PseudoClass CLASS_FAIL = PseudoClass.getPseudoClass("fail");

    private SearchFieldClassTool() {
        // Prevent instantiation - all methods are private
    }

    public static void updateStateClass(final Node node, final boolean isFail) {
        node.pseudoClassStateChanged(CLASS_FAIL, isFail);
    }
}

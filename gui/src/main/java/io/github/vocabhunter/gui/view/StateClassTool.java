/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import io.github.vocabhunter.analysis.marked.WordState;
import javafx.css.PseudoClass;
import javafx.scene.Node;

public final class StateClassTool {
    private static final PseudoClass CLASS_UNSEEN = PseudoClass.getPseudoClass("unseen");

    private static final PseudoClass CLASS_KNOWN = PseudoClass.getPseudoClass("known");

    private static final PseudoClass CLASS_UNKNOWN = PseudoClass.getPseudoClass("unknown");

    private StateClassTool() {
        // Prevent instantiation - all methods are private
    }

    public static void clearStateClasses(final Node node) {
        node.pseudoClassStateChanged(CLASS_UNSEEN, false);
        node.pseudoClassStateChanged(CLASS_KNOWN, false);
        node.pseudoClassStateChanged(CLASS_UNKNOWN, false);
    }

    public static void updateStateClasses(final Node node, final WordState state) {
        node.pseudoClassStateChanged(CLASS_UNSEEN, state.equals(WordState.UNSEEN));
        node.pseudoClassStateChanged(CLASS_KNOWN, state.equals(WordState.KNOWN));
        node.pseudoClassStateChanged(CLASS_UNKNOWN, state.equals(WordState.UNKNOWN));
    }
}

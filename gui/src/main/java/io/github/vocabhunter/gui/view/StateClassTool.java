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

    private static final PseudoClass CLASS_EXCLUDED = PseudoClass.getPseudoClass("excluded");

    private StateClassTool() {
        // Prevent instantiation - all methods are private
    }

    public static void clearStateClasses(final Node node) {
        updateStateClasses(node, null);
    }

    public static void updateStateClasses(final Node node, final WordState state) {
        node.pseudoClassStateChanged(CLASS_UNSEEN, state == WordState.UNSEEN);
        node.pseudoClassStateChanged(CLASS_KNOWN, state == WordState.KNOWN);
        node.pseudoClassStateChanged(CLASS_UNKNOWN, state == WordState.UNKNOWN);
    }

    public static void clearExtendedStateClasses(final Node node) {
        clearStateClasses(node);
        clearExcludedClass(node);
    }

    public static void updateExtendedStateClasses(final Node node, final WordState state, final boolean isExcluded) {
        if (isExcluded) {
            clearStateClasses(node);
        } else {
            updateStateClasses(node, state);
        }
        updatedExcludedClass(node, isExcluded);
    }

    public static void updatedExcludedClass(final Node node, final boolean isExcluded) {
        node.pseudoClassStateChanged(CLASS_EXCLUDED, isExcluded);
    }

    public static void clearExcludedClass(final Node node) {
        updatedExcludedClass(node, false);
    }
}

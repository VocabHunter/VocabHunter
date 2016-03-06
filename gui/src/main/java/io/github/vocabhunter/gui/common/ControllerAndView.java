/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import javafx.scene.Node;

public final class ControllerAndView<C, V extends Node> {
    private final C controller;

    private final V view;

    public ControllerAndView(final C controller, final V view) {
        this.controller = controller;
        this.view = view;
    }

    public C getController() {
        return controller;
    }

    public V getView() {
        return view;
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public final class SessionActions {
    private final EventHandler<KeyEvent> keyPressHandler;

    private final Runnable openSearchAction;

    public SessionActions(final EventHandler<KeyEvent> keyPressHandler, final Runnable openSearchAction) {
        this.keyPressHandler = keyPressHandler;
        this.openSearchAction = openSearchAction;
    }

    public EventHandler<KeyEvent> getKeyPressHandler() {
        return keyPressHandler;
    }

    public Runnable getOpenSearchAction() {
        return openSearchAction;
    }
}

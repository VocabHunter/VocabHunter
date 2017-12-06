/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import javafx.event.Event;
import javafx.event.EventHandler;

public final class EventHandlerTool {
    private EventHandlerTool() {
        // Prevent instantiation - all methods are static
    }

    public static <T extends Event> EventHandler<T> combine(final EventHandler<T> h1, final EventHandler<T> h2, final EventHandler<T> h3) {
        return e -> handle(e, h1, h2, h3);
    }

    private static <T extends Event> void handle(final T e, final EventHandler<T> h1, final EventHandler<T> h2, final EventHandler<T> h3) {
        h1.handle(e);
        h2.handle(e);
        h3.handle(e);
    }
}

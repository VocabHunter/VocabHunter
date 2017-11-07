/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.List;

public class AppenderForTesting extends AppenderBase<ILoggingEvent> {
    private final List<String> messages = new ArrayList<>();

    @Override
    protected void append(final ILoggingEvent event) {
        messages.add(event.getFormattedMessage());
    }

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }
}

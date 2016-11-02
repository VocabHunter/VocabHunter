/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.osx;

import com.google.inject.AbstractModule;
import io.github.vocabhunter.gui.event.CommandLineEventSource;
import io.github.vocabhunter.gui.event.ExternalEventBroker;
import io.github.vocabhunter.gui.event.ExternalEventSource;

public class OsxEventSourceModule extends AbstractModule {
    private final String[] args;

    public OsxEventSourceModule(final String... args) {
        this.args = args;
    }

    @Override
    protected void configure() {
        ExternalEventSource commandLineEventSource = new CommandLineEventSource(args);
        OsxEventSource osxEventSource = new OsxEventSource();
        ExternalEventBroker externalEventBroker = new ExternalEventBroker();

        commandLineEventSource.setListener(externalEventBroker);
        osxEventSource.setListener(externalEventBroker);
        bind(ExternalEventSource.class).toInstance(externalEventBroker);
    }
}

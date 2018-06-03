/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.osx;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.github.vocabhunter.analysis.core.ThreadPoolTool;
import io.github.vocabhunter.gui.event.CommandLineEventSource;
import io.github.vocabhunter.gui.event.ExternalEventBroker;
import io.github.vocabhunter.gui.event.ExternalEventBrokerImpl;

import java.util.List;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;

public class OsxEventSourceModule extends AbstractModule {
    private final List<String> args;

    public OsxEventSourceModule(final String... args) {
        this.args = listOf(args);
    }

    @Override
    protected void configure() {
        bind(CommandLineEventSource.class).toInstance(new CommandLineEventSource(args));
    }

    @Provides
    public ExternalEventBroker provideExternalEventBroker(final CommandLineEventSource commandLineEventSource, final OsxEventSource osxEventSource, final ThreadPoolTool threadPoolTool) {
        ExternalEventBrokerImpl externalEventBroker = new ExternalEventBrokerImpl(threadPoolTool);

        commandLineEventSource.setListener(externalEventBroker);
        osxEventSource.setListener(externalEventBroker);

        return externalEventBroker;
    }
}

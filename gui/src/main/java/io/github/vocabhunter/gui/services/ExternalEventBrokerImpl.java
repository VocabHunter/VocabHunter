/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import java.nio.file.Path;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ExternalEventBrokerImpl implements ExternalEventBroker {
    private enum BrokerStateComponent {
        FILE_RECEIVED, DISPLAY_SHOWN, GUI_OPEN
    }

    private final AtomicReference<Set<BrokerStateComponent>> state = new AtomicReference<>(EnumSet.noneOf(BrokerStateComponent.class));

    private final AtomicReference<Path> fileReference = new AtomicReference<>();

    private volatile Consumer<Path> fileProcessor;

    @Override
    public void openFile(final Path file) {
        fileReference.set(file);
        transitionState(BrokerStateComponent.FILE_RECEIVED);
    }

    @Override
    public void markMainDisplayShown() {
        transitionState(BrokerStateComponent.DISPLAY_SHOWN);
    }

    @Override
    public void markGuiOpen(final Consumer<Path> fileProcessor) {
        this.fileProcessor = fileProcessor;
        transitionState(BrokerStateComponent.GUI_OPEN);
    }

    private void transitionState(final BrokerStateComponent component) {
        Set<BrokerStateComponent> oldState;
        Set<BrokerStateComponent> newState;

        do {
            oldState = state.get();
            newState = addStateComponent(oldState, component);
        }
        while (!state.compareAndSet(oldState, newState));

        if (newState.equals(EnumSet.allOf(BrokerStateComponent.class))) {
            Path file = fileReference.getAndSet(null);

            if (file != null) {
                fileProcessor.accept(file);
            }
        }
    }

    private Set<BrokerStateComponent> addStateComponent(final Collection<BrokerStateComponent> oldSet, final BrokerStateComponent component) {
        var newSet = EnumSet.copyOf(oldSet);

        newSet.add(component);

        return newSet;
    }
}

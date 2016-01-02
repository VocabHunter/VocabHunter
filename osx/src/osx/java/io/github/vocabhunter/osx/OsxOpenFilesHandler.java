/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.osx;

import com.apple.eawt.AppEvent.OpenFilesEvent;
import com.apple.eawt.OpenFilesHandler;
import io.github.vocabhunter.gui.event.ExternalEventListener;
import io.github.vocabhunter.gui.event.ExternalOpenFileEvent;

import java.io.File;

public class OsxOpenFilesHandler implements OpenFilesHandler {
    private final ExternalEventListener delegate;

    public OsxOpenFilesHandler(final ExternalEventListener delegate) {
        this.delegate = delegate;
    }

    @Override
    public void openFiles(final OpenFilesEvent event) {
        event.getFiles().stream()
                .map(File::toPath)
                .map(ExternalOpenFileEvent::new)
                .forEach(delegate::fireOpenFileEvent);
    }
}

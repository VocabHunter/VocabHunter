/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.osx;

import com.apple.eawt.Application;
import io.github.vocabhunter.gui.event.ExternalEventListener;
import io.github.vocabhunter.gui.event.SingleExternalEventSource;

import javax.inject.Singleton;

@Singleton
public class OsxEventSource implements SingleExternalEventSource {
    @Override
    public void setListener(final ExternalEventListener listener) {
        Application application = Application.getApplication();

        application.setOpenFileHandler(new OsxOpenFilesHandler(listener));
    }
}

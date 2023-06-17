/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.gui.common.Placement;
import jakarta.inject.Singleton;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import org.apache.commons.lang3.SystemUtils;

@Singleton
public class EnvironmentManagerImpl implements EnvironmentManager {
    @Override
    public Placement getScreenSize() {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        return new Placement(visualBounds.getWidth(), visualBounds.getHeight());
    }

    @Override
    public boolean isVisible(final Placement placement) {
        ObservableList<Screen> screens = Screen.getScreensForRectangle(rectangle(placement));

        if (screens.size() == 1) {
            Screen screen = screens.get(0);
            Rectangle2D bounds = screen.getVisualBounds();

            if (placement.positioned()) {
                return bounds.contains(placement.x(), placement.y(), placement.width(), placement.height());
            } else {
                return bounds.getWidth() >= placement.width() && bounds.getHeight() >= placement.height();
            }
        } else {
            return false;
        }
    }

    private Rectangle2D rectangle(final Placement placement) {
        if (placement.positioned()) {
            return new Rectangle2D(placement.x(), placement.y(), placement.width(), placement.height());
        } else {
            return new Rectangle2D(0, 0, placement.width(), placement.height());
        }
    }

    @Override
    public boolean useSystemMenuBar() {
        return true;
    }

    @Override
    public boolean isExitOptionShown() {
        return !SystemUtils.IS_OS_MAC_OSX;
    }
}

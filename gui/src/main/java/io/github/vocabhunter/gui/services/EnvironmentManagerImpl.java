/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.gui.common.Placement;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import org.apache.commons.lang3.SystemUtils;

import javax.inject.Singleton;

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

            if (placement.isPositioned()) {
                return bounds.contains(placement.getX(), placement.getY(), placement.getWidth(), placement.getHeight());
            } else {
                return bounds.getWidth() >= placement.getWidth() && bounds.getHeight() >= placement.getHeight();
            }
        } else {
            return false;
        }
    }

    private Rectangle2D rectangle(final Placement placement) {
        if (placement.isPositioned()) {
            return new Rectangle2D(placement.getX(), placement.getY(), placement.getWidth(), placement.getHeight());
        } else {
            return new Rectangle2D(0, 0, placement.getWidth(), placement.getHeight());
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

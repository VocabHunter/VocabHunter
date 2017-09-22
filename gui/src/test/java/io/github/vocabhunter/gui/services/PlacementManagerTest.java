/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.gui.common.Placement;
import io.github.vocabhunter.gui.settings.SettingsManager;
import io.github.vocabhunter.gui.settings.WindowSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static io.github.vocabhunter.gui.services.PlacementManagerImpl.WINDOW_SIZE_FACTOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PlacementManagerTest {
    private static final Placement SCREEN_SIZE = new Placement(1, 2);

    private static final Placement DEFAULT_WINDOW_SIZE = new Placement(SCREEN_SIZE.getWidth() * WINDOW_SIZE_FACTOR, SCREEN_SIZE.getHeight() * WINDOW_SIZE_FACTOR);

    private final WindowSettings windowSettings = new WindowSettings();

    @Mock
    private EnvironmentManager environmentManager;

    @Mock
    private SettingsManager settingsManager;

    @InjectMocks
    private PlacementManagerImpl target;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setUp() {
        windowSettings.setWidth(1);
        windowSettings.setHeight(2);
        windowSettings.setX(3);
        windowSettings.setY(4);
        when(environmentManager.getScreenSize()).thenReturn(SCREEN_SIZE);
    }

    @Test
    public void testVisibleSettings() {
        when(environmentManager.isVisible(any(Placement.class))).thenReturn(true);
        when(settingsManager.getWindowSettings()).thenReturn(Optional.of(windowSettings));

        Placement result = target.getMainWindow();

        assertEquals(result, new Placement(1, 2, 3, 4), "Visible settings");
    }

    @Test
    public void testInvisibleSettings() {
        when(environmentManager.isVisible(any(Placement.class))).thenReturn(false);
        when(settingsManager.getWindowSettings()).thenReturn(Optional.of(windowSettings));

        Placement result = target.getMainWindow();

        assertEquals(result, DEFAULT_WINDOW_SIZE, "Invisible settings");
    }

    @Test
    public void testUnspecifiedSettings() {
        when(settingsManager.getWindowSettings()).thenReturn(Optional.empty());

        Placement result = target.getMainWindow();

        assertEquals(result, DEFAULT_WINDOW_SIZE, "Unspecified settings");
    }
}

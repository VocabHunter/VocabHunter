/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.settings;

import io.github.vocabhunter.test.utils.AbstractBeanTest;

public class WindowSettingsTest extends AbstractBeanTest<WindowSettings> {

    @Override
    protected WindowSettings buildPrimary() {
        return new WindowSettings();
    }

    @Override
    protected WindowSettings buildSecondary() {
        WindowSettings settings = buildPrimary();

        settings.setX(settings.getX() + 1);

        return settings;
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import java.awt.*;

public class EnvironmentManagerImpl implements EnvironmentManager {
    @Override
    public Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    @Override
    public boolean useSystemMenuBar() {
        return true;
    }
}

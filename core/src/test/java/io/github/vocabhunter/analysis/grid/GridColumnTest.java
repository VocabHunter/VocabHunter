/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.test.utils.AbstractBeanTest;

public class GridColumnTest extends AbstractBeanTest<GridColumn> {
    public static final GridColumn PRIMARY = new GridColumn(1);

    public static final GridColumn SECONDARY = new GridColumn(2);

    @Override
    protected GridColumn buildPrimary() {
        return PRIMARY;
    }

    @Override
    protected GridColumn buildSecondary() {
        return SECONDARY;
    }
}

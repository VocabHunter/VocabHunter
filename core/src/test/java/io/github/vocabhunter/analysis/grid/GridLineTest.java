/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.test.utils.AbstractBeanTest;

public class GridLineTest extends AbstractBeanTest<GridLine> {
    public static final GridLine PRIMARY = new GridLine(GridCellTest.PRIMARY);

    public static final GridLine SECONDARY = new GridLine(GridCellTest.SECONDARY);

    @Override
    protected GridLine buildPrimary() {
        return PRIMARY;
    }

    @Override
    protected GridLine buildSecondary() {
        return SECONDARY;
    }
}

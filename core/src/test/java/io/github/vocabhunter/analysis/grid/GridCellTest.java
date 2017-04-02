/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.test.utils.AbstractBeanTest;

public class GridCellTest extends AbstractBeanTest<GridCell> {
    public static final GridCell PRIMARY = new GridCell("text", true);

    public static final GridCell SECONDARY = new GridCell("text", false);

    @Override
    protected GridCell buildPrimary() {
        return PRIMARY;
    }

    @Override
    protected GridCell buildSecondary() {
        return SECONDARY;
    }
}

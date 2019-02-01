/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.test.utils.AbstractBeanTest;

import java.util.List;

public class TextGridTest extends AbstractBeanTest<TextGrid> {
    @Override
    protected TextGrid buildPrimary() {
        return new TextGrid(List.of(GridLineTest.PRIMARY), List.of(GridColumnTest.PRIMARY));
    }

    @Override
    protected TextGrid buildSecondary() {
        return new TextGrid(List.of(GridLineTest.SECONDARY), List.of(GridColumnTest.SECONDARY));
    }
}

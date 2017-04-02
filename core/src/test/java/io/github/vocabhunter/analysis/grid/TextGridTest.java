/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.test.utils.AbstractBeanTest;

import static java.util.Collections.singletonList;

public class TextGridTest extends AbstractBeanTest<TextGrid> {
    @Override
    protected TextGrid buildPrimary() {
        return new TextGrid(singletonList(GridLineTest.PRIMARY), singletonList(GridColumnTest.PRIMARY));
    }

    @Override
    protected TextGrid buildSecondary() {
        return new TextGrid(singletonList(GridLineTest.SECONDARY), singletonList(GridColumnTest.SECONDARY));
    }
}

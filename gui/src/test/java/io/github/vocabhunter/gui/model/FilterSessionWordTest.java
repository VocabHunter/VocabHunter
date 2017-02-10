/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.test.utils.AbstractBeanTest;

public class FilterSessionWordTest extends AbstractBeanTest<FilterSessionWord> {
    @Override
    protected FilterSessionWord buildPrimary() {
        return new FilterSessionWord("Word 1", WordState.KNOWN);
    }

    @Override
    protected FilterSessionWord buildSecondary() {
        return new FilterSessionWord("Word 2", WordState.KNOWN);
    }
}

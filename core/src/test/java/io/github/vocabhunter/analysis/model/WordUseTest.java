/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.model;

import io.github.vocabhunter.test.utils.AbstractBeanTest;

import static java.util.Collections.emptyList;

public class WordUseTest extends AbstractBeanTest<WordUse> {
    @Override
    protected WordUse buildPrimary() {
        return new WordUse("Word_1", 0, emptyList());
    }

    @Override
    protected WordUse buildSecondary() {
        return new WordUse("Word_2", 0, emptyList());
    }
}

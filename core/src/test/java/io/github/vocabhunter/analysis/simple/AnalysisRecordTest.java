/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.simple;

import io.github.vocabhunter.test.utils.AbstractBeanTest;

public class AnalysisRecordTest extends AbstractBeanTest<AnalysisRecord> {
    @Override
    protected AnalysisRecord buildPrimary() {
        return new AnalysisRecord("WORD", 0);
    }

    @Override
    protected AnalysisRecord buildSecondary() {
        return new AnalysisRecord("word", 0);
    }
}

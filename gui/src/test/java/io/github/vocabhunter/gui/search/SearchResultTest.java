/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.search;

import io.github.vocabhunter.gui.common.TestSequencedWord;
import io.github.vocabhunter.test.utils.AbstractBeanTest;

public class SearchResultTest extends AbstractBeanTest<SearchResult<TestSequencedWord>> {

    @Override
    protected SearchResult<TestSequencedWord> buildPrimary() {
        return new SearchResult<>("", null, null, null, true);
    }

    @Override
    protected SearchResult<TestSequencedWord> buildSecondary() {
        return new SearchResult<>("", null, null, null, false);
    }
}

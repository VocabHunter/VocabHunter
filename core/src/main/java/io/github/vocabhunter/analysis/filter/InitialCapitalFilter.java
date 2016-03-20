/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import io.github.vocabhunter.analysis.model.AnalysisWord;

public final class InitialCapitalFilter implements WordFilter {
    @Override
    public boolean isShown(final AnalysisWord word) {
        String identifier = word.getWordIdentifier();

        return identifier.isEmpty() || Character.isLowerCase(identifier.charAt(0));
    }
}

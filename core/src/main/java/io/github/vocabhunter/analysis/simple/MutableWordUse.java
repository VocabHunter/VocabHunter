/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.simple;

import io.github.vocabhunter.analysis.core.PreferredFormTool;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.model.WordUse;

import java.util.ArrayList;
import java.util.List;

public class MutableWordUse {
    private String wordIdentifier;

    private final List<Integer> lineNos = new ArrayList<>();

    public void accumulate(final AnalysisRecord record) {
        if (wordIdentifier == null) {
            wordIdentifier = record.getIdentifier();
        } else {
            wordIdentifier = PreferredFormTool.preferredForm(wordIdentifier, record.getIdentifier());
        }
        lineNos.add(record.getLine());
    }

    public MutableWordUse combine(final MutableWordUse that) {
        if (wordIdentifier == null) {
            wordIdentifier = that.wordIdentifier;
        } else if (that.wordIdentifier != null) {
            wordIdentifier = PreferredFormTool.preferredForm(wordIdentifier, that.wordIdentifier);
        }
        lineNos.addAll(that.lineNos);

        return this;
    }

    public WordUse toWordUse() {
        if (wordIdentifier == null) {
            throw new VocabHunterException("Invalid collector operation");
        } else {
            return new WordUse(wordIdentifier, lineNos.size(), lineNos);
        }
    }
}

/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.analysis.settings.BaseListedFile;
import io.github.vocabhunter.gui.model.FilterFileModel;

public interface FilterFileModelTranslator {
    BaseListedFile fromModel(FilterFileModel model);

    FilterFileModel toModel(BaseListedFile file);
}

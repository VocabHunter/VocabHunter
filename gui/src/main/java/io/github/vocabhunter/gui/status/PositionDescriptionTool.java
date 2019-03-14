/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import io.github.vocabhunter.gui.model.PositionModel;
import io.github.vocabhunter.gui.model.ProgressModel;

import java.text.MessageFormat;
import javax.inject.Singleton;

@Singleton
public class PositionDescriptionTool {
    private static final int POSITION_BUFFER_SIZE = 100;

    public String describe(final PositionModel position, final ProgressModel progress) {
        if (position.isAnalysisMode()) {
            StringBuilder buffer = new StringBuilder(POSITION_BUFFER_SIZE);

            buffer.append(MessageFormat.format("Word {0} of {1} {1,choice,0#words|1#word|1<words}", position.getPositionIndex() + 1, position.getSize()));
            if (position.isEditable()) {
                int filtered = progress.unseenFilteredProperty().get();

                if (filtered > 0) {
                    buffer.append(MessageFormat.format(" ({0} {0,choice,0#words|1#word|1<words} hidden by filter)", filtered));
                }
            } else {
                buffer.append(" marked as unknown");
            }

            return buffer.toString();
        } else {
            return "";
        }
    }
}

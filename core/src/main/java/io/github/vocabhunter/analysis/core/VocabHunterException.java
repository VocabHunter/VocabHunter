/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

public class VocabHunterException extends RuntimeException {
    public VocabHunterException(final String message) {
        super(message);
    }

    public VocabHunterException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

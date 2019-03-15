/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.i18n;

import io.github.vocabhunter.analysis.core.CoreConstants;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ObservableValue;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javax.inject.Singleton;

@Singleton
public class I18nManagerImpl implements I18nManager {
    public static final String BUNDLE_BASE_NAME = "bundles/VocabHunterBundle";

    private final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME);

    @Override
    public ResourceBundle bundle() {
        return bundle;
    }

    @Override
    public void initialise() {
        Locale.setDefault(CoreConstants.LOCALE);
    }

    @Override
    public String text(final I18nKey key, final Object... arguments) {
        String template = bundle.getString(key.getKey());

        return MessageFormat.format(template, arguments);
    }

    @Override
    public StringExpression textBinding(final I18nKey key, final ObservableValue<?>... arguments) {
        return Bindings.createStringBinding(() -> text(key, toArgs(arguments)), arguments);
    }

    private Object[] toArgs(final ObservableValue<?>... observables) {
        return Stream.of(observables)
            .map(ObservableValue::getValue)
            .toArray();
    }
}

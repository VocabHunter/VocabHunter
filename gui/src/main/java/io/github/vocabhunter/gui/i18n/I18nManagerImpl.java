/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.i18n;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ObservableValue;

import java.text.MessageFormat;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javax.inject.Singleton;

@Singleton
public class I18nManagerImpl implements I18nManager {
    private static final String BUNDLE_BASE_NAME = "bundles/VocabHunterBundle";

    private final Map<SupportedLocale, ResourceBundle> bundles = new EnumMap<>(SupportedLocale.class);

    private SupportedLocale locale;

    public I18nManagerImpl() {
        Locale.setDefault(SupportedLocale.DEFAULT_LOCALE.getLocale());

        Stream.of(SupportedLocale.values())
            .forEach(this::loadBundle);
    }

    private void loadBundle(final SupportedLocale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale.getLocale());

        bundles.put(locale, bundle);
    }

    public static I18nManager createForDefaultLocale() {
        I18nManager result = new I18nManagerImpl();

        result.setupLocale(SupportedLocale.DEFAULT_LOCALE);

        return result;
    }

    @Override
    public ResourceBundle bundle() {
        return bundles.get(locale);
    }

    @Override
    public void setupLocale(final SupportedLocale locale) {
        Locale.setDefault(locale.getLocale());

        this.locale = locale;
    }

    @Override
    public String text(final I18nKey key, final Object... arguments) {
        return text(locale, key, arguments);
    }

    @Override
    public String text(final SupportedLocale explicitLocale, final I18nKey key, final Object... arguments) {
        String template = bundles.get(explicitLocale).getString(key.getKey());

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

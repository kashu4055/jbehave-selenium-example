package example.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;

import org.jbehave.core.i18n.LocalizedKeywords;

public final class LocaleSetter {
    
    private LocaleSetter() {
        // singleton
    }

    /**
     * Setzt die Locale für JBehave auf eine schmutzige Art.
     * Aber der offizielle Weg zum Setzen der Locale ist fehleranfällig und schlecht wartbar.
     * @param locale gewünschte Story-Sprache
     */
    public static void setDefaultLocale(Locale locale) {
        try {
            Field localeField = LocalizedKeywords.class.getDeclaredField("DEFAULT_LOCALE");
            setPrivateFinal(localeField, locale);
        } catch (NoSuchFieldException | SecurityException e) {
            throw new AutomationException("Could not set default locale", e);
        }
    }

    private static void setPrivateFinal(Field field, Object newValue) {
        field.setAccessible(true);
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, newValue);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new AutomationException("Could not set default locale", e);
        }
    }

}

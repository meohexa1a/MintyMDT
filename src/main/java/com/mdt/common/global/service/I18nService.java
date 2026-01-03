package com.mdt.common.global.service;

import io.micronaut.context.MessageSource;
import io.micronaut.context.annotation.Requires;
import jakarta.annotation.Nonnull;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mindustry.gen.Player;

import java.util.Locale;

/**
 * I18n service using Micronaut {@link MessageSource}.
 * <p>
 * Supports both: <p>
 * - Micronaut-style: {0} → use {@code tr(...)} <p>
 * - Java format: %s → use {@code trs(...)}
 * <p>
 * Falls back to key if missing.
 */
@Slf4j
@Singleton
@Requires(beans = MessageSource.class)
@RequiredArgsConstructor
public final class I18nService {
    private final MessageSource messageSource;

    // !---------------------------------------------------------!

    /**
     * Translate with Micronaut-style formatting ({@code {0}}).
     */
    public String tr(String key, @Nonnull String lang, Object... args) {
        var locale = Locale.forLanguageTag(lang.replace("_", "-"));
        return messageSource.getMessage(key, locale, args).orElse(key);
    }

    /**
     * Translate using player’s locale (Micronaut-style).
     */
    public String tr(String key, Player player, Object... args) {
        return tr(key, player != null ? player.locale() : "en", args);
    }

    /**
     * Translate then format with Java-style placeholders ({@code %s}).
     */
    public String trs(String key, String lang, Object... args) {
        return String.format(tr(key, lang), args);
    }

    /**
     * Translate using player’s locale, then format with Java-style.
     */
    public String trs(String key, Player player, Object... args) {
        return String.format(tr(key, player), args);
    }
}

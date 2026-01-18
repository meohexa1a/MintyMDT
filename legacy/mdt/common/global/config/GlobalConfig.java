package com.mdt.common.global.config;

import io.micronaut.context.MessageSource;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.i18n.ResourceBundleMessageSource;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Factory
@RequiredArgsConstructor
final class GlobalConfig {

    @Singleton
    MessageSource messageSource() {
        return new ResourceBundleMessageSource("i18n.messages");
    }
}

package com.mdt;

import arc.util.CommandHandler;
import mindustry.mod.Plugin;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MintyMDTPlugin extends Plugin {
    private static @Getter CommandHandler serverHandler, clientHandler = null;
    private static @Getter ApplicationContext context = null;
    private static volatile boolean isStarted = false;

    // !----------------------------------------------------------------!

    @Override
    public void registerServerCommands(CommandHandler handler) {
        MintyMDTPlugin.serverHandler = handler;
        init();
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        MintyMDTPlugin.clientHandler = handler;
        init();
    }

    public synchronized void init() {
        if (isStarted || serverHandler == null || clientHandler == null) return;
        MintyMDTPlugin.isStarted = true;

        log.info("<!-- MintyMDT Plugin Framework --!>");

        try {
            MintyMDTPlugin.context = startContext();
        } catch (Exception e) {
            log.error("Failed to initialize Micronaut context. Server inactive, please manual shutdown!", e);
        }
    }

    // !----------------------------------------------------------------!

    private ApplicationContext startContext() {
        try {
            log.info("Attempting to start Micronaut with AOT metadata...");

            return ApplicationContext.builder()
                    .eagerInitSingletons(true)
                    .start();
        } catch (Exception aotFailed) {
            log.warn("AOT context not found, falling back to runtime scanning...");

            return ApplicationContext.builder()
                    .eagerInitSingletons(true)
                    .packages("com.mdt")
                    .start();
        }
    }

    // !----------------------------------------------------------------!

    @Factory
    public static class MintyMDT {

        @Bean
        ApplicationContext applicationContext() {
            return MintyMDTPlugin.getContext();
        }
    }
}
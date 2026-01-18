package com.mdt;

import com.mdt.common.annotation.Prototype;
import arc.util.CommandHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mindustry.mod.Plugin;
import org.codejargon.feather.Feather;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public final class MintyMDTPlugin extends Plugin {
    private static @Getter CommandHandler serverHandler, clientHandler = null;
    private static @Getter Feather feather;
    private volatile boolean isStarted = false;

    // !--------------------------------------------------------!

    @Override
    public void registerServerCommands(CommandHandler handler) {
        MintyMDTPlugin.serverHandler = handler;
        initMintyMDT();
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        MintyMDTPlugin.clientHandler = handler;
        initMintyMDT();
    }

    // !--------------------------------------------------------!

    private synchronized void initMintyMDT() {
        if (isStarted || serverHandler == null || clientHandler == null) return;
        this.isStarted = true;

        try {
            MintyMDTPlugin.feather = Feather.with(new HashSet<>() {{
                addAll(scanAnnotatedClasses());

                add(serverHandler);
                add(clientHandler);
            }});
        } catch (Exception e) {
            log.error("Failed to initialize Dependency Injection. Exit application", e);
            System.exit(-1);
        }

        log.info("MintyMDT Plugin Framework - v3.0");
    }

    private Set<Class<?>> scanAnnotatedClasses() {
        return new HashSet<>() {{
            var reflections = new Reflections("com.mdt", Scanners.TypesAnnotated);

            addAll(reflections.getTypesAnnotatedWith(Prototype.class));
            addAll(reflections.getTypesAnnotatedWith(Singleton.class));
        }};
    }
}
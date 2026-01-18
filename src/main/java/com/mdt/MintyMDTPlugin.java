package com.mdt;

import mindustry.mod.Plugin;
import org.codejargon.feather.Feather;

import arc.util.CommandHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MintyMDTPlugin extends Plugin {
    private static @Getter CommandHandler serverHandler, clientHandler = null;
    private volatile boolean isStarted = false;

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
        this.isStarted = true;

        
    }
}

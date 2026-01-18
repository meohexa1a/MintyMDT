package com.mdt.mindustry.command.service;

import com.mdt.mindustry.command.type.ClientCommand;
import com.mdt.mindustry.command.type.ConsoleCommand;
import com.mdt.mindustry.command.type.GlobalCommandProvider;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Singleton
@RequiredArgsConstructor
final class GlobalCommandRegister {
    private final Set<GlobalCommandProvider> providers;

    private final CommandRegisterService registerService;

    // !-------------------------------------------------------------!

    @PostConstruct
    void init() {
        Set<ClientCommand> clientCommands = new HashSet<>();
        Set<ConsoleCommand> consoleCommands = new HashSet<>();

        for (var provider : providers) {
            clientCommands.addAll(provider.clientCommands());
            consoleCommands.addAll(provider.consoleCommands());
        }

        registerService.registerCommands("global", clientCommands, consoleCommands);
    }
}

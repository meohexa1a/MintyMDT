package com.mdt.mindustry.command.service;

import com.mdt.MintyMDTPlugin;
import com.mdt.mindustry.command.type.ClientCommand;
import com.mdt.mindustry.command.type.ConsoleCommand;
import com.mdt.common.shared.type.Pair;
import jakarta.inject.Singleton;
import lombok.Locked;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import mindustry.gen.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Singleton
public final class CommandRegisterService {
    private final Map<String, Pair<Set<ClientCommand>, Set<ConsoleCommand>>> registered = new HashMap<>();

    // !---------------------------------------------------------------------!

    @Locked
    public void registerCommands(String group,
            @NonNull Set<ClientCommand> clientCommands, @NonNull Set<ConsoleCommand> consoleCommands) {
        if (registered.containsKey(group)) unregister(group);

        registered.put(group, new Pair<>(clientCommands, consoleCommands));
        clientCommands.forEach(this::registerClient);
        consoleCommands.forEach(this::registerConsole);
    }

    @Locked
    public void unregister(String group) {
        var pair = registered.remove(group);
        if (pair == null) return;

        pair.first().forEach(clientCommand ->
                clientCommand.prefixes().forEach(MintyMDTPlugin.getClientHandler()::removeCommand));
        pair.second().forEach(consoleCommand ->
                consoleCommand.prefixes().forEach(MintyMDTPlugin.getServerHandler()::removeCommand));
    }

    // !--------------------------------------------------------!

    private void registerClient(ClientCommand cmd) {
        for (var prefix : cmd.prefixes())
            MintyMDTPlugin.getClientHandler().<Player>register(prefix, cmd.args(), cmd.description(), (args, player) -> {
                try {
                    cmd.action().accept(args, player);
                } catch (Exception e) {
                    log.error("Error while executing command | {}", prefix, e);
                }
            });
    }

    private void registerConsole(ConsoleCommand cmd) {
        for (String prefix : cmd.prefixes())
            MintyMDTPlugin.getServerHandler().register(prefix, cmd.args(), cmd.description(), (args) -> {
                try {
                    cmd.action().accept(args);
                } catch (Exception ex) {
                    log.error("Error while executing console command | {}", prefix, ex);
                }
            });
    }
}


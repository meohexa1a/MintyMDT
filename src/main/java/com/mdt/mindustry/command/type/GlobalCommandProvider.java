package com.mdt.mindustry.command.type;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

import java.util.Set;

@Builder(toBuilder = true)
public record GlobalCommandProvider(
        @Singular Set<ClientCommand> clientCommands,
        @Singular Set<ConsoleCommand> consoleCommands) {

}

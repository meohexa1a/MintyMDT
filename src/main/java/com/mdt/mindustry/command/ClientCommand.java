package com.mdt.mindustry.command;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import mindustry.gen.Player;

import java.util.Set;
import java.util.function.BiConsumer;

@Builder(toBuilder = true)
public record ClientCommand(
        @NonNull @Singular Set<String> prefixes,
        @NonNull String description,
        @NonNull String args,
        @NonNull BiConsumer<String[], Player> action) {

}

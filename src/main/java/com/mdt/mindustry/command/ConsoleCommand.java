package com.mdt.mindustry.command;

import lombok.Builder;
import lombok.Generated;
import lombok.NonNull;
import lombok.Singular;

import java.util.Set;
import java.util.function.Consumer;

@Builder(toBuilder = true)
public record ConsoleCommand(
    @NonNull @Singular Set<String> prefixes,
    @NonNull String description,
    @NonNull String args,
    @NonNull Consumer<String[]> action) {

    public static class ConsoleCommandBuilder {
        private @Generated String description = "";
        private @Generated String args = "";
    }
}

package com.mdt.common.type;

import org.jetbrains.annotations.NotNull;

public record Pair<A, B>(A first, B second) {

    public static <A, B> Pair<A, B> of(@NotNull A first, @NotNull B second) {
        return new Pair<>(first, second);
    }
}

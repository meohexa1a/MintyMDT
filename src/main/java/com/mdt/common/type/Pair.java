package com.mdt.common.type;

import lombok.NonNull;

public record Pair<A, B>(A first, B second) {

    public static <A, B> Pair<A, B> of(@NonNull A first, @NonNull B second) {
        return new Pair<>(first, second);
    }
}

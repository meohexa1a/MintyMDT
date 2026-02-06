package com.mdt.common.signal;

import com.mdt.common.type.Unit;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public sealed interface Result<T, F extends Failure> {

    @Contract(value = "_ -> new", pure = true)
    static <T, F extends Failure> @NotNull Result<T, F> success(@NotNull T value) {
        return new Success<>(value);
    }

    @Contract(value = " -> new", pure = true)
    static <F extends Failure> @NotNull Result<Unit, F> ok() {
        return new Success<>(Unit.INSTANCE);
    }

    @Contract(value = " -> new", pure = true)
    static <T, F extends Failure> @NotNull Result<T, F> empty() {
        return new Empty<>();
    }

    @Contract(value = "_ -> new", pure = true)
    static <T, F extends Failure> @NotNull Result<T, F> error(@NotNull F failure) {
        return new Error<>(failure);
    }

    static <T, F extends Failure> @NotNull Result<T, F> of(Supplier<@NotNull T> supplier, @NotNull T fallback) {
        try {
            return new Success<>(supplier.get());
        } catch (Exception e) {
            return new Success<>(fallback);
        }
    }

    // !----------------------------------------------------------------------------!

    @Contract(pure = true)
    default <U> Result<U, F> map(Function<? super T, ? extends U> fn) {
        return switch (this) {
            case Result.Success<T, F> s -> new Success<>(fn.apply(s.value()));
            case Empty<T, F> ignore -> new Empty<>();
            case Error<T, F> e -> new Error<>(e.failure());
        };
    }

    @Contract(pure = true)
    default Result<T, F> provide(Supplier<? extends @NotNull T> supplier) {
        return switch (this) {
            case Success<T, F> s -> s;
            case Empty<T, F> ignore -> new Success<>(supplier.get());
            case Error<T, F> e -> e;
        };
    }

    @Contract(pure = true)
    default Result<T, F> fallIfEmpty(Supplier<@NotNull F> supplier) {
        return switch (this) {
            case Success<T, F> s -> s;
            case Empty<T, F> ignore -> new Error<>(supplier.get());
            case Error<T, F> e -> e;
        };
    }

    @Contract(pure = true)
    default Result<T, F> recover(Function<? super F, ? extends T> fn) {
        return switch (this) {
            case Success<T, F> s -> s;
            case Empty<T, F> e -> e;
            case Error<T, F> err -> new Success<>(fn.apply(err.failure()));
        };
    }

    @Contract(pure = true)
    default <G extends Failure> Result<T, G> mapError(Function<? super F, ? extends G> fn) {
        return switch (this) {
            case Success<T, F> s -> new Success<>(s.value());
            case Empty<T, F> ignore -> new Empty<>();
            case Error<T, F> e -> new Error<>(fn.apply(e.failure()));
        };
    }


    @Contract(pure = true)
    default <U> Result<U, F> flatMap(Function<? super T, @NotNull Result<U, F>> fn) {
        return switch (this) {
            case Success<T, F> s -> fn.apply(s.value());
            case Empty<T, F> ignore -> new Empty<>();
            case Error<T, F> e -> new Error<>(e.failure());
        };
    }

    @Contract(pure = true)
    default Result<T, F> provideWith(Supplier<Result<T, F>> supplier) {
        return switch (this) {
            case Success<T, F> s -> s;
            case Empty<T, F> ignore -> supplier.get();
            case Error<T, F> e -> e;
        };
    }

    @Contract(pure = true)
    default Result<T, F> recoverWith(Function<F, @NotNull Result<T, F>> fn) {
        return switch (this) {
            case Success<T, F> s -> s;
            case Empty<T, F> e -> e;
            case Error<T, F> err -> fn.apply(err.failure());
        };
    }

    default <G extends Failure> Result<T, G> flatMapError(Function<F, @NotNull Result<T, G>> fn) {
        return switch (this) {
            case Success<T, F> s -> new Success<>(s.value());
            case Empty<T, F> ignore -> new Empty<>();
            case Error<T, F> e -> fn.apply(e.failure());
        };
    }

    @Contract(pure = true)
    default <U> Success<U, F> foldToSuccess(
        Function<T, @NotNull U> onSuccess,
        Function<F, @NotNull U> onError,
        Supplier<@NotNull U> onEmpty) {
        return switch (this) {
            case Success<T, F> s -> new Success<>(onSuccess.apply(s.value()));
            case Error<T, F> e -> new Success<>(onError.apply(e.failure()));
            case Empty<T, F> ignored -> new Success<>(onEmpty.get());
        };
    }

    // !---------------------------------------------------------------!

    default <R> R fold(
        Function<? super T, ? extends R> onSuccess,
        Function<? super F, ? extends R> onError,
        Supplier<? extends R> onEmpty) {
        return switch (this) {
            case Success<T, F> s -> onSuccess.apply(s.value());
            case Error<T, F> e -> onError.apply(e.failure());
            case Empty<T, F> ignored -> onEmpty.get();
        };
    }

    // !---------------------------------------------------------------!

    default Result<T, F> onSuccess(Consumer<? super T> c) {
        if (this instanceof Success<T, F>(T value)) c.accept(value);
        return this;
    }

    default Result<T, F> onEmpty(Runnable r) {
        if (this instanceof Empty<T, F>) r.run();
        return this;
    }

    default Result<T, F> onError(Consumer<? super F> c) {
        if (this instanceof Error<T, F>(F failure)) c.accept(failure);
        return this;
    }

    // !---------------------------------------------------------------!

    record Success<T, F extends Failure>(@NotNull T value) implements Result<T, F>{
    }

    record Empty<T, F extends Failure>() implements Result<T, F> {
    }

    record Error<T, F extends Failure>(@NotNull F failure) implements Result<T, F> {
    }
}

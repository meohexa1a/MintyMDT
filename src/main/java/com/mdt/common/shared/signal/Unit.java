package com.mdt.common.shared.signal;

/**
 * Represents a type that holds no meaningful data except for its existence.
 * <p>
 * The {@code Unit} type has only one single instance: {@link #INSTANCE}.
 * It is primarily used in functional programming and reactive streams to signal
 * that an operation has completed successfully when no return value is needed.
 *
 * <h3>Why use Unit over Boolean?</h3>
 * <ul>
 * <li><b>Definite State:</b> A {@link Boolean} object can represent three states:
 * {@code true}, {@code false}, or {@code null}. {@code Unit} simplifies this by
 * representing a binary outcome: {@link #INSTANCE} (Success/Present) or {@code null} (Failure/Absent).</li>
 * <li><b>Intent:</b> It explicitly communicates that the <i>value</i> of the result
 * is irrelevant, only the <i>occurrence</i> of the event matters.</li>
 * </ul>
 *
 * <h3>Why use Unit over Void?</h3>
 * <ul>
 * <li>Unlike {@link Void}, which cannot be instantiated, {@code Unit} is a real object.
 * This makes it compatible with generic APIs that require a non-null return value
 * to trigger observers or success callbacks.</li>
 * </ul>
 */
public final class Unit {
    public static final Unit INSTANCE = new Unit();

    // !--------------------------------------------!

    private Unit() {

    }

    // !--------------------------------------------!

    @Override
    public String toString() {
        return "Unit";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Unit;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
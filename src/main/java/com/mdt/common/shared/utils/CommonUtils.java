package com.mdt.common.shared.utils;

import com.mdt.common.shared.signal.Unit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonUtils {

    /**
     * A method that does... nothing? YES!
     *
     * @param ignored the arguments to be ignored
     */
    public static void doNothing(Object... ignored) {

    }

    public static Unit returnUnit(Object... ignored) {
        return Unit.INSTANCE;
    }

    public static boolean returnTrue(Object... ignored) {
        return true;
    }

    public static boolean returnFalse(Object... ignored) {
        return false;
    }
}

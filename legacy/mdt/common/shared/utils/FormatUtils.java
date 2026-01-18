package com.mdt.common.shared.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FormatUtils {

    /**
     * Formats the given time in milliseconds to a good readable string.
     *
     * @param millis the time in milliseconds
     * @return the formatted time string
     */
    public static String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        minutes = minutes % 60;
        seconds = seconds % 60;

        if (hours > 0) return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        else if (minutes > 0) return String.format("%02dm %02ds", minutes, seconds);
        else return String.format("%02ds", seconds);
    }
}

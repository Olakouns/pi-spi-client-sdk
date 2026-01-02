package io.github.olakouns.util;

import java.util.Date;

public final class Time {
    private static volatile int offset;

    private Time() {}

    /**
     * Returns current time in seconds adjusted by adding {@link #offset) seconds.
     * @return see description
     */
    public static int currentTime() {
        return ((int) (System.currentTimeMillis() / 1000)) + offset;
    }

    /**
     * Returns current time in milliseconds adjusted by adding {@link #offset) seconds.
     * @return see description
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis() + (offset * 1000L);
    }

    /**
     * Returns {@link Date} object, its value set to time
     * @param time Time in milliseconds since the epoch
     * @return see description
     */
    public static Date toDate(int time) {
        return new Date(time * 1000L);
    }

    /**
     * Returns {@link Date} object, its value set to time
     * @param time Time in milliseconds since the epoch
     * @return see description
     */
    public static Date toDate(long time) {
        return new Date(time);
    }

    /**
     * Returns time in milliseconds for a time in seconds. No adjustment is made to the parameter.
     * @param time Time in seconds since the epoch
     * @return Time in milliseconds
     */
    public static long toMillis(long time) {
        return time * 1000L;
    }

    /**
     * @return Time offset in seconds that will be added to {@link #currentTime()} and {@link #currentTimeMillis()}.
     */
    public static int getOffset() {
        return offset;
    }

    /**
     * Sets time offset in seconds that will be added to {@link #currentTime()} and {@link #currentTimeMillis()}.
     * @param offset Offset (in seconds)
     */
    public static void setOffset(int offset) {
        Time.offset = offset;
    }

}

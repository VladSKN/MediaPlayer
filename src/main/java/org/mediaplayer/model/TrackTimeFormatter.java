package org.mediaplayer.model;

/** Formats microsecond playback positions for display. */
public final class TrackTimeFormatter {
    private static final long MICROS_PER_SECOND = 1_000_000L;

    private TrackTimeFormatter() {
    }

    public static String formatMicros(long microseconds) {
        long totalSeconds = Math.max(0, microseconds) / MICROS_PER_SECOND;
        long hours = totalSeconds / 3_600;
        long minutes = (totalSeconds / 60) % 60;
        long seconds = totalSeconds % 60;
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", totalSeconds / 60, seconds);
    }
}

package org.mediaplayer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrackTimeFormatterTest {
    @Test
    void formatsDurationsAsMinutesAndSeconds() {
        assertEquals("00:00", TrackTimeFormatter.formatMicros(0));
        assertEquals("01:01", TrackTimeFormatter.formatMicros(61_000_000));
    }

    @Test
    void formatsLongDurationsWithHoursAndClampsNegativeValues() {
        assertEquals("1:01:01", TrackTimeFormatter.formatMicros(3_661_000_000L));
        assertEquals("00:00", TrackTimeFormatter.formatMicros(-1));
    }
}

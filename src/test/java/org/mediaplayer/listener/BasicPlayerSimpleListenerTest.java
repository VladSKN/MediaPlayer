package org.mediaplayer.listener;

import org.junit.jupiter.api.Test;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicPlayerSimpleListenerTest {
    @Test
    void sendsProgressUpdatesToTheEdtAndReportsTheCorrectFraction() throws Exception {
        JSlider slider = new JSlider(0, 1_000, 0);
        AtomicBoolean updateRanOnEdt = new AtomicBoolean(false);
        BasicPlayerSimpleListener listener = new BasicPlayerSimpleListener(
                slider, new JLabel(), value -> {
                    updateRanOnEdt.set(SwingUtilities.isEventDispatchThread());
                    slider.setValue(value);
                });

        listener.opened(null, Collections.singletonMap("duration", 120_000_000L));
        listener.progress(0, 60_000_000L, null, Collections.emptyMap());
        SwingUtilities.invokeAndWait(() -> { });

        assertEquals(500, slider.getValue());
        assertTrue(updateRanOnEdt.get());
    }
}

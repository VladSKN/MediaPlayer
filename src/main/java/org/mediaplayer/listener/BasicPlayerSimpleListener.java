package org.mediaplayer.listener;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import org.mediaplayer.model.TrackTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import java.util.Map;
import java.util.function.IntConsumer;

/** Bridges BasicPlayer's playback thread to Swing controls on the EDT. */
public class BasicPlayerSimpleListener implements BasicPlayerListener {
    private static final int SLIDER_MAXIMUM = 1_000;

    private final JSlider positionSlider;
    private final JLabel songTimeLabel;
    private final IntConsumer sliderPositionUpdater;
    private volatile long durationMicros;

    public BasicPlayerSimpleListener(JSlider positionSlider, JLabel songTimeLabel, IntConsumer sliderPositionUpdater) {
        this.positionSlider = positionSlider;
        this.songTimeLabel = songTimeLabel;
        this.sliderPositionUpdater = sliderPositionUpdater;
    }

    @Override
    public void opened(Object source, Map properties) {
        Object duration = properties == null ? null : properties.get("duration");
        durationMicros = duration instanceof Number ? Math.max(0, ((Number) duration).longValue()) : 0;
        long total = durationMicros;
        SwingUtilities.invokeLater(() -> {
            sliderPositionUpdater.accept(0);
            positionSlider.setEnabled(total > 0);
            songTimeLabel.setText(TrackTimeFormatter.formatMicros(0) + "/"
                    + TrackTimeFormatter.formatMicros(total));
        });
    }

    @Override
    public void progress(int bytesRead, long microseconds, byte[] pcmData, Map properties) {
        long position = Math.max(0, microseconds);
        long duration = durationMicros;
        int sliderValue = duration > 0
                ? (int) Math.min(SLIDER_MAXIMUM, Math.round(position * (double) SLIDER_MAXIMUM / duration))
                : 0;
        SwingUtilities.invokeLater(() -> {
            if (!positionSlider.getValueIsAdjusting()) {
                sliderPositionUpdater.accept(sliderValue);
            }
            songTimeLabel.setText(TrackTimeFormatter.formatMicros(position) + "/"
                    + TrackTimeFormatter.formatMicros(duration));
        });
    }

    @Override
    public void stateUpdated(BasicPlayerEvent event) {
        // Playback state is controlled by the view; no component work is needed here.
    }

    @Override
    public void setController(BasicController controller) {
        // BasicPlayer calls this during listener registration.
    }
}

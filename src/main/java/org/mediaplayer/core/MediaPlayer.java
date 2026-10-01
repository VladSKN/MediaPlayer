package org.mediaplayer.core;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import java.io.File;
import java.util.Objects;

/** BasicPlayer adapter with file selection, seek, and a stable 0–100 volume scale. */
public class MediaPlayer extends BasicPlayer {
    private volatile File currentTrack;
    private volatile int volumePercent = 70;

    public synchronized void play(File track) throws BasicPlayerException {
        Objects.requireNonNull(track, "track");
        if (!track.isFile()) {
            throw new IllegalArgumentException("Audio file does not exist: " + track);
        }

        stop();
        currentTrack = null;
        open(track);
        currentTrack = track;
        setGain(volumePercent / 100.0);
        super.play();
    }

    @Override
    public synchronized void stop() throws BasicPlayerException {
        super.stop();
        Thread playbackThread = m_thread;
        if (playbackThread == null || playbackThread == Thread.currentThread() || !playbackThread.isAlive()) {
            return;
        }
        try {
            playbackThread.join(1_000);
            if (playbackThread.isAlive()) {
                playbackThread.interrupt();
                playbackThread.join(250);
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new BasicPlayerException(BasicPlayerException.WAITERROR, exception);
        }
        if (playbackThread.isAlive()) {
            throw new BasicPlayerException(BasicPlayerException.WAITERROR);
        }
    }

    public void pauseOrResume() throws BasicPlayerException {
        if (getStatus() == PAUSED) {
            resume();
        } else if (getStatus() == PLAYING) {
            pause();
        } else if (currentTrack != null) {
            play(currentTrack);
        }
    }

    public void setVolumePercent(int value) throws BasicPlayerException {
        volumePercent = Math.max(0, Math.min(100, value));
        int status = getStatus();
        if (status == OPENED || status == PLAYING || status == PAUSED) {
            setGain(volumePercent / 100.0);
        }
    }

    public int getVolumePercent() {
        return volumePercent;
    }

    public File getCurrentTrack() {
        return currentTrack;
    }

    /** Seeks to a fraction of the encoded file; BasicPlayer's seek API uses bytes. */
    public void seekToFraction(double fraction) throws BasicPlayerException {
        File track = currentTrack;
        if (track == null || track.length() == 0) {
            return;
        }
        double clampedFraction = Math.max(0.0, Math.min(1.0, fraction));
        seek(Math.round(track.length() * clampedFraction));
    }
}

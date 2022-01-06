package org.mediaplayer.core;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import java.io.File;

public class MediaPlayer extends BasicPlayer {
    private final int MAX_VOLUME_RATIO = 10;

    @Override
    public void play() {
        try {
            String music1 = "25 Gunther - Ding Dong Song.mp3";
            // todo: MP-123 добавить название песни как аргумент
            open(new File(music1));
            super.play();
        } catch (BasicPlayerException b) {
            b.printStackTrace();
        }
    }

    @Override
    public void pause() {
        try {
            super.pause();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    public void pauseOrResume() {
        if (getStatus() == PAUSED) {
            resume();
        } else {
            pause();
        }
    }

    @Override
    public void stop() {
        try {
            super.stop();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resume() {
        try {
            super.resume();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    public void setVolume(int volume) throws BasicPlayerException {
        setGain(volume / 100.0);
    }

    @Override
    public float getMaximumGain() {
        return super.getMaximumGain() * MAX_VOLUME_RATIO;
    }

    public int getSongLength()
    {
        return m_audioFileFormat.getFrameLength();
    }
}

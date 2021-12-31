import java.io.File;
import java.util.Map;

import javazoom.jlgui.basicplayer.*;

import javax.swing.*;


public class Sound implements BasicPlayerListener {
    private final BasicPlayer player = new BasicPlayer();
    private boolean isPlaying = false;
    private final int RATIO = 10;
    private final JProgressBar progressBar;

    public Sound(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public void opened(Object o, Map map) {
        String k = map.get("audio.length.frames").toString();
        progressBar.setMaximum(Integer.parseInt(k) - 15);
    }

    @Override
    public void progress(int arg0, long arg1, byte[] arg2, Map map) {
        String s = map.get("mp3.frame").toString();
        int p = 0;
        int k = (p - (Integer.parseInt(s)) * (-1));
        progressBar.setValue(k);
    }

    @Override
    public void stateUpdated(BasicPlayerEvent basicPlayerEvent) {
    }

    @Override
    public void setController(BasicController basicController) {
    }

    public void play() {
        try {
            player.addBasicPlayerListener(this);
            String music1 = "25 Gunther - Ding Dong Song.mp3";
            player.open(new File(music1));
            player.play();
            isPlaying = true;
        } catch (BasicPlayerException b) {
            b.printStackTrace();
        }
    }

    public void pause() {
        try {
            if (isPlaying) {
                player.pause();
                isPlaying = false;
            } else {
                player.resume();
                isPlaying = true;
            }
        } catch (BasicPlayerException b) {
            b.printStackTrace();
        }
    }

    public void stop() {
        try {
            player.stop();
            isPlaying = false;
        } catch (BasicPlayerException b) {
            b.printStackTrace();
        }
    }

    public void setVolume(double volume) throws BasicPlayerException {
        player.setGain(volume / 100);
    }


    public double getMaximumGain() {
        return player.getMaximumGain() * RATIO;
    }
}




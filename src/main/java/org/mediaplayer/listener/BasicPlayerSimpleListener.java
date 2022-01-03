package org.mediaplayer.listener;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import org.mediaplayer.model.SongTimeModel;

import javax.swing.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class BasicPlayerSimpleListener implements BasicPlayerListener {
    private final JProgressBar progressBar;

    private final JLabel songTimeLabel;


    public BasicPlayerSimpleListener(JProgressBar progressBar, JLabel songTimeLabel) {
        this.progressBar = progressBar;
        this.songTimeLabel = songTimeLabel;
    }

    @Override
    public void opened(Object o, Map map) {
        String k = map.get("audio.length.frames").toString();

        long songInMicroSeconds = (long) map.get("duration");
        long minutes = TimeUnit.MICROSECONDS.toMinutes(songInMicroSeconds);
        long seconds = TimeUnit.MICROSECONDS.toSeconds(songInMicroSeconds) - TimeUnit.MINUTES.toSeconds(minutes);

        SongTimeModel.getInstance().setTotalSeconds((int) seconds);
        SongTimeModel.getInstance().setTotalMinutes((int) minutes);

        drawCurrentTime();
    }

    private void drawCurrentTime() {
        SongTimeModel model = SongTimeModel.getInstance();
        songTimeLabel.setText(String.format("%02d:%02d/%02d:%02d",
                model.getPassedMinutes(), model.getPassedSeconds(), model.getTotalMinutes(), model.getTotalSeconds()));
    }

    @Override
    public void progress(int arg0, long arg1, byte[] arg2, Map map) {
//        String s = map.get("mp3.frame").toString();

        long songInMicroSeconds = (long) map.get("mp3.position.microseconds");
        long minutes = TimeUnit.MICROSECONDS.toMinutes(songInMicroSeconds);
        long seconds = TimeUnit.MICROSECONDS.toSeconds(songInMicroSeconds) - TimeUnit.MINUTES.toSeconds(minutes);

//        int p = 0;
//        int k = (p - Integer.parseInt(s) * -1);

        long passedSeconds = seconds + TimeUnit.MINUTES.toSeconds(minutes);
        long totalSeconds = SongTimeModel.getInstance().getTotalSeconds()
                + TimeUnit.MINUTES.toSeconds(SongTimeModel.getInstance().getTotalMinutes());

        progressBar.setValue((int) ((passedSeconds * 1.0 / totalSeconds) * 100));

        SongTimeModel.getInstance().setPassedSeconds((int) seconds);
        SongTimeModel.getInstance().setPassedMinutes((int) minutes);

        drawCurrentTime();
    }

    @Override
    public void stateUpdated(BasicPlayerEvent basicPlayerEvent) {
    }

    @Override
    public void setController(BasicController basicController) {
    }
}



package org.mediaplayer;

import org.mediaplayer.core.MediaPlayer;
import org.mediaplayer.view.MediaPlayerView;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        MediaPlayer mediaPlayer = new MediaPlayer();

        MediaPlayerView dialog = new MediaPlayerView(mediaPlayer);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                mediaPlayer.stop();
                dialog.dispose();
            }
        });

        dialog.pack();
        dialog.setVisible(true);
    }
}

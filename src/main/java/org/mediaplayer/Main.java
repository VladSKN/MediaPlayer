package org.mediaplayer;

import org.mediaplayer.core.MediaPlayer;
import org.mediaplayer.view.MediaPlayerView;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MediaPlayer mediaPlayer = new MediaPlayer();
            MediaPlayerView window = new MediaPlayerView(mediaPlayer);
            window.setVisible(true);
        });
    }
}

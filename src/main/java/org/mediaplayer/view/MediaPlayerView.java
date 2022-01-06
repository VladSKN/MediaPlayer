package org.mediaplayer.view;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import org.mediaplayer.core.MediaPlayer;
import org.mediaplayer.listener.BasicPlayerSimpleListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class MediaPlayerView extends JDialog {
    private final MediaPlayer mediaPlayer;

    private JPanel contentPane;

    private JButton buttonPlay;

    private JPanel mediaPlayerPanel;

    private JProgressBar progressBar;

    private JButton buttonStop;

    private JButton buttonLeft;

    private JButton buttonRight;

    private JScrollBar scrollBar1;

    private JButton buttonPause;

    private JButton addButton;

    private JSlider volumeSlider;
    private JSlider slider1;
    private JLabel songTimeLabel;

    public MediaPlayerView(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;

        this.mediaPlayer.addBasicPlayerListener(new BasicPlayerSimpleListener(progressBar, songTimeLabel));

        progressBar.setMinimum(0);
        progressBar.setMaximum(100);

        setContentPane(contentPane);
        setModal(true);

        volumeSlider.addChangeListener(e -> {
            int value = ((JSlider) e.getSource()).getValue();
            try {
                volumeSlider.setMaximum((int) mediaPlayer.getMaximumGain());
                volumeSlider.setMinimum(0);
                mediaPlayer.setVolume(value);
            } catch (BasicPlayerException ex) {
                ex.printStackTrace();
            }
        });

        buttonPlay.addActionListener(e -> mediaPlayer.play());

        buttonPause.addActionListener(e -> mediaPlayer.pauseOrResume());

        buttonStop.addActionListener(e -> mediaPlayer.stop());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        contentPane.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

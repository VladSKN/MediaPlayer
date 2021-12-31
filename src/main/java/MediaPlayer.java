import javazoom.jlgui.basicplayer.BasicPlayerException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;


public class MediaPlayer extends JDialog {
    private JPanel contentPane;
    private JButton buttonPlay;
    private JPanel MediaPlayer;
    private JProgressBar progressBar;
    private JButton buttonStop;
    private JButton buttonLeft;
    private JButton buttonRight;
    private JScrollBar scrollBar1;
    private JButton buttonPause;
    private JButton addButton;
    private JSlider volumeSlider;
    private final Sound sound = new Sound(progressBar);

    public MediaPlayer() {
        setContentPane(contentPane);
        setModal(true);


        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double value = ((JSlider) e.getSource()).getValue();
                try {
                    volumeSlider.setMaximum((int) sound.getMaximumGain());
                    volumeSlider.setMinimum(0);
                    sound.setVolume(value);
                } catch (BasicPlayerException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sound.play();
            }
        });

        buttonPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sound.pause();
            }
        });

        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sound.stop();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

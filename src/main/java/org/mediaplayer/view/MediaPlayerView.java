package org.mediaplayer.view;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import org.mediaplayer.core.MediaPlayer;
import org.mediaplayer.listener.BasicPlayerSimpleListener;
import org.mediaplayer.model.Playlist;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/** Runtime-built Swing UI; no IDE-specific .form instrumentation is required. */
public class MediaPlayerView extends JFrame {
    private final MediaPlayer mediaPlayer;
    private final Playlist playlist = new Playlist();
    private final DefaultListModel<File> listModel = new DefaultListModel<>();
    private final JList<File> playlistView = new JList<>(listModel);
    private final JSlider positionSlider = new JSlider(0, 1_000, 0);
    private final JSlider volumeSlider = new JSlider(0, 100, 70);
    private final JLabel songTimeLabel = new JLabel("00:00/00:00", SwingConstants.CENTER);
    private final JLabel currentTrackLabel = new JLabel("Добавьте аудиофайлы в плейлист");
    private boolean updatingPosition;

    public MediaPlayerView(MediaPlayer mediaPlayer) {
        super("Музыкальный проигрыватель");
        this.mediaPlayer = mediaPlayer;
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setContentPane(createContentPane());
        setMinimumSize(new Dimension(480, 340));
        setSize(600, 430);
        setLocationByPlatform(true);

        mediaPlayer.addBasicPlayerListener(new BasicPlayerSimpleListener(
                positionSlider, songTimeLabel, this::setPositionFromPlayer));
        configureActions();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                closePlayer();
            }
        });
    }

    private JPanel createContentPane() {
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        playlistView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playlistView.setCellRenderer((list, file, index, selected, focused) -> {
            JLabel label = new JLabel(file.getName());
            label.setOpaque(true);
            label.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 8, 5, 8));
            if (selected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }
            return label;
        });
        content.add(new JScrollPane(playlistView), BorderLayout.CENTER);

        JPanel playback = new JPanel(new BorderLayout(6, 4));
        currentTrackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playback.add(currentTrackLabel, BorderLayout.NORTH);
        positionSlider.setEnabled(false);
        JPanel positionPanel = new JPanel(new BorderLayout(8, 0));
        positionPanel.add(songTimeLabel, BorderLayout.WEST);
        positionPanel.add(positionSlider, BorderLayout.CENTER);
        playback.add(positionPanel, BorderLayout.CENTER);

        JPanel controls = new JPanel(new GridLayout(1, 5, 6, 0));
        JButton previousButton = new JButton("Предыдущий");
        JButton playButton = new JButton("Play");
        JButton pauseButton = new JButton("Пауза / продолжить");
        JButton stopButton = new JButton("Stop");
        JButton nextButton = new JButton("Следующий");
        controls.add(previousButton);
        controls.add(playButton);
        controls.add(pauseButton);
        controls.add(stopButton);
        controls.add(nextButton);
        playback.add(controls, BorderLayout.SOUTH);
        content.add(playback, BorderLayout.SOUTH);

        JPanel top = new JPanel(new BorderLayout(8, 0));
        JButton addButton = new JButton("Добавить файлы…");
        JLabel volumeLabel = new JLabel("Громкость");
        JPanel volume = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        volume.add(volumeLabel);
        volume.add(volumeSlider);
        top.add(addButton, BorderLayout.WEST);
        top.add(volume, BorderLayout.EAST);
        content.add(top, BorderLayout.NORTH);

        addButton.addActionListener(event -> chooseFiles());
        previousButton.addActionListener(event -> moveAndPlay(false));
        playButton.addActionListener(event -> playSelected());
        pauseButton.addActionListener(event -> pauseOrResume());
        stopButton.addActionListener(event -> stopPlayback());
        nextButton.addActionListener(event -> moveAndPlay(true));
        return content;
    }

    private void configureActions() {
        playlistView.addListSelectionListener(this::selectionChanged);
        playlistView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    playSelected();
                }
            }
        });
        positionSlider.addChangeListener(event -> {
            if (!updatingPosition && !positionSlider.getValueIsAdjusting()) {
                try {
                    mediaPlayer.seekToFraction(positionSlider.getValue() / (double) positionSlider.getMaximum());
                } catch (BasicPlayerException exception) {
                    showError("Не удалось перемотать трек", exception);
                }
            }
        });
        volumeSlider.addChangeListener(event -> {
            try {
                mediaPlayer.setVolumePercent(volumeSlider.getValue());
            } catch (BasicPlayerException exception) {
                showError("Не удалось изменить громкость", exception);
            }
        });
    }

    private void chooseFiles() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Добавить аудиофайлы");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(true);
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter(
                "Аудиофайлы (MP3, WAV, AIFF)", "mp3", "wav", "aif", "aiff"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File[] selectedFiles = chooser.getSelectedFiles();
        if (selectedFiles == null || selectedFiles.length == 0) {
            File selectedFile = chooser.getSelectedFile();
            selectedFiles = selectedFile == null ? new File[0] : new File[]{selectedFile};
        }
        int oldSize = playlist.getTracks().size();
        playlist.addAll(Arrays.asList(selectedFiles));
        List<File> tracks = playlist.getTracks();
        for (int index = oldSize; index < tracks.size(); index++) {
            listModel.addElement(tracks.get(index));
        }
        if (oldSize == 0 && !tracks.isEmpty()) {
            playlistView.setSelectedIndex(0);
        }
    }

    private void selectionChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            int selectedIndex = playlistView.getSelectedIndex();
            File selected = playlist.select(selectedIndex);
            if (selected != null) {
                currentTrackLabel.setText(selected.getName());
            }
        }
    }

    private void playSelected() {
        int selectedIndex = playlistView.getSelectedIndex();
        File track = selectedIndex >= 0 ? playlist.select(selectedIndex) : playlist.current();
        if (track == null) {
            showError("Сначала добавьте аудиофайл в плейлист", null);
            return;
        }
        playTrack(track);
    }

    private void playTrack(File track) {
        try {
            mediaPlayer.play(track);
            currentTrackLabel.setText(track.getName());
        } catch (BasicPlayerException | IllegalArgumentException exception) {
            showError("Не удалось воспроизвести «" + track.getName() + "»", exception);
        }
    }

    private void moveAndPlay(boolean forward) {
        File track = forward ? playlist.next() : playlist.previous();
        if (track == null) {
            return;
        }
        int index = playlist.getTracks().indexOf(track);
        playlistView.setSelectedIndex(index);
        playlistView.ensureIndexIsVisible(index);
        playTrack(track);
    }

    private void pauseOrResume() {
        try {
            mediaPlayer.pauseOrResume();
        } catch (BasicPlayerException exception) {
            showError("Не удалось поставить на паузу или продолжить воспроизведение", exception);
        }
    }

    private void stopPlayback() {
        try {
            mediaPlayer.stop();
            setPositionFromPlayer(0);
        } catch (BasicPlayerException exception) {
            showError("Не удалось остановить воспроизведение", exception);
        }
    }

    private void setPositionFromPlayer(int value) {
        updatingPosition = true;
        try {
            positionSlider.setValue(value);
        } finally {
            updatingPosition = false;
        }
    }

    private void closePlayer() {
        try {
            mediaPlayer.stop();
        } catch (BasicPlayerException exception) {
            // A stopped or never-opened player needs no further cleanup.
        }
        dispose();
    }

    private void showError(String message, Exception exception) {
        String details = exception == null || exception.getMessage() == null
                ? message
                : message + ": " + exception.getMessage();
        javax.swing.JOptionPane.showMessageDialog(this, details, "MediaPlayer", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

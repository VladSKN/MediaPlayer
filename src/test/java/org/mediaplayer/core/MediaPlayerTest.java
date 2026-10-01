package org.mediaplayer.core;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MediaPlayerTest {
    @Test
    void volumeIsClampedBeforeAnAudioFileIsOpened() throws Exception {
        MediaPlayer player = new MediaPlayer();

        player.setVolumePercent(140);
        assertEquals(100, player.getVolumePercent());

        player.setVolumePercent(-1);
        assertEquals(0, player.getVolumePercent());
    }

    @Test
    void rejectsMissingTracksWithoutOpeningThem() {
        MediaPlayer player = new MediaPlayer();

        assertThrows(IllegalArgumentException.class,
                () -> player.play(new File("this-file-does-not-exist.mp3")));
    }
}

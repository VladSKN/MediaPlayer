package org.mediaplayer.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PlaylistTest {
    @TempDir
    Path tempDir;

    @Test
    void addingTracksSelectsFirstAndDoesNotAddDuplicates() throws Exception {
        File first = Files.createFile(tempDir.resolve("first.mp3")).toFile();
        File second = Files.createFile(tempDir.resolve("second.mp3")).toFile();
        Playlist playlist = new Playlist();

        int added = playlist.addAll(Arrays.asList(first, second, first, null));

        assertEquals(2, added);
        assertEquals(first, playlist.current());
        assertEquals(Arrays.asList(first, second), playlist.getTracks());
    }

    @Test
    void nextAndPreviousWrapAroundThePlaylist() throws Exception {
        File first = Files.createFile(tempDir.resolve("first.mp3")).toFile();
        File second = Files.createFile(tempDir.resolve("second.mp3")).toFile();
        Playlist playlist = new Playlist();
        playlist.addAll(Arrays.asList(first, second));

        assertEquals(second, playlist.next());
        assertEquals(first, playlist.next());
        assertEquals(second, playlist.previous());
    }

    @Test
    void navigationOnAnEmptyPlaylistReturnsNull() {
        Playlist playlist = new Playlist();

        assertNull(playlist.current());
        assertNull(playlist.next());
        assertNull(playlist.previous());
    }
}

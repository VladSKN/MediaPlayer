package org.mediaplayer.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/** Maintains the tracks and current selection for the player. */
public final class Playlist {
    private final List<File> tracks = new ArrayList<>();
    private int currentIndex = -1;

    /** Adds non-null tracks once and selects the first track when the playlist was empty. */
    public int addAll(Collection<File> files) {
        if (files == null) {
            return 0;
        }

        int added = 0;
        for (File file : files) {
            if (file != null && !tracks.contains(file)) {
                tracks.add(file);
                added++;
            }
        }
        if (currentIndex < 0 && !tracks.isEmpty()) {
            currentIndex = 0;
        }
        return added;
    }

    public List<File> getTracks() {
        return Collections.unmodifiableList(new ArrayList<>(tracks));
    }

    public File current() {
        return currentIndex >= 0 ? tracks.get(currentIndex) : null;
    }

    public File select(int index) {
        if (index < 0 || index >= tracks.size()) {
            return null;
        }
        currentIndex = index;
        return current();
    }

    public File next() {
        if (tracks.isEmpty()) {
            return null;
        }
        currentIndex = (currentIndex + 1) % tracks.size();
        return current();
    }

    public File previous() {
        if (tracks.isEmpty()) {
            return null;
        }
        currentIndex = (currentIndex - 1 + tracks.size()) % tracks.size();
        return current();
    }
}

package org.mediaplayer.model;

public class SongTimeModel {
    private volatile static SongTimeModel instance;

    private int passedMinutes;

    private int passedSeconds;

    private int totalMinutes;

    private int totalSeconds;

    private SongTimeModel() {
    }

    public int getPassedMinutes() {
        return passedMinutes;
    }

    public void setPassedMinutes(int passedMinutes) {
        this.passedMinutes = passedMinutes;
    }

    public int getPassedSeconds() {
        return passedSeconds;
    }

    public void setPassedSeconds(int passedSeconds) {
        this.passedSeconds = passedSeconds;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public int getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(int totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public static SongTimeModel getInstance() {
        if (instance == null) {
            synchronized (SongTimeModel.class) {
                if (instance == null) {
                    SongTimeModel newInstance = new SongTimeModel();
                    instance = newInstance;
                }
            }
        }
        return instance;
    }
}

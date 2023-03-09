package com.example.audionotes.service;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

public class MediaPlayService {

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private String fileAbsolutePath;
    private MediaPlayer.OnCompletionListener onCompletionListener =
            new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    isPlaying = false;
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            };

    public MediaPlayService() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(onCompletionListener);
    }

    public void setFileAbsolutePath(String fileAbsolutePath) {
        this.fileAbsolutePath = fileAbsolutePath;

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this.fileAbsolutePath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void start() {

        if (isPlaying) {
            stop();
        }

        if (fileAbsolutePath.equals("") || fileAbsolutePath.length() != 0) {
            isPlaying = true;
            mediaPlayer.start();
        }
    }

    public int getProgress() {
        return mediaPlayer.getCurrentPosition();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void stop() {
        isPlaying = false;
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    public int length() {
        return mediaPlayer.getDuration();
    }
}

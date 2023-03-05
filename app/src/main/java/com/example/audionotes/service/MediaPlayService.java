package com.example.audionotes.service;

import android.media.MediaPlayer;
import android.os.Handler;
import java.io.IOException;

public class MediaPlayService {

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Handler handler;
    private Runnable run;
    private OnProgress onProgress;

    public MediaPlayService(OnProgress onProgress) {
        this.onProgress = onProgress;
    }

    public void start(String fileAbsolutePath) {
        try {
            if (isPlaying) {
                stop();
            }
            handler = new Handler();
            run = new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        onProgress.progress(mediaPlayer.getCurrentPosition());
                        handler.postDelayed(this, 100);
                    }
                }
            };
            handler.postDelayed(run, 0);
            isPlaying = true;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fileAbsolutePath);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        isPlaying = false;
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
        mediaPlayer = null;
    }

    public int length() {
        return mediaPlayer.getDuration();
    }
}

package com.example.audionotes.service;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.IOException;

public class MediaRecorderService {

    private MediaRecorder mediaRecorder;
    private String fileName;

    private String filePath;

    public MediaRecorderService() {
        mediaRecorder = new MediaRecorder();
    }

    public String getFileName() {
        return fileName;
    }

    public MediaRecorderService setFileName(String fileName) {
        this.fileName = fileName.trim();
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public MediaRecorderService setFilePath(String filePath) {
        this.filePath = filePath.trim();
        return this;
    }

    public void start(){

        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(filePath + fileName);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }
}

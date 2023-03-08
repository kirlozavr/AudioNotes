package com.example.audionotes.dto;

public class AudioNoteDto {

    private long id;
    private String titleNote;
    private String dateCreateNote;
    private String timeCreateNote;
    private String filePath;
    private String fileName;
    private int timingAudio;
    private boolean isPlay;

    public AudioNoteDto(
            String titleNote,
            String dateCreateNote,
            String timeCreateNote,
            String filePath,
            String fileName,
            int timingAudio,
            boolean isPlay
    ) {
        this.titleNote = titleNote;
        this.dateCreateNote = dateCreateNote;
        this.timeCreateNote = timeCreateNote;
        this.filePath = filePath;
        this.fileName = fileName;
        this.timingAudio = timingAudio;
        this.isPlay = isPlay;
    }

    public AudioNoteDto(
            long id,
            String titleNote,
            String dateCreateNote,
            String timeCreateNote,
            String filePath,
            String fileName,
            int timingAudio,
            boolean isPlay
    ) {
        this.id = id;
        this.titleNote = titleNote;
        this.dateCreateNote = dateCreateNote;
        this.timeCreateNote = timeCreateNote;
        this.filePath = filePath;
        this.fileName = fileName;
        this.timingAudio = timingAudio;
        this.isPlay = isPlay;
    }

    public long getId() {
        return id;
    }

    public String getTitleNote() {
        return titleNote;
    }

    public void setTitleNote(String titleNote) {
        this.titleNote = titleNote;
    }

    public String getDateCreateNote() {
        return dateCreateNote;
    }

    public void setDateCreateNote(String dateCreateNote) {
        this.dateCreateNote = dateCreateNote;
    }

    public String getTimeCreateNote() {
        return timeCreateNote;
    }

    public void setTimeCreateNote(String timeCreateNote) {
        this.timeCreateNote = timeCreateNote;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getTimingAudio() {
        return timingAudio;
    }

    public void setTimingAudio(int timingAudio) {
        this.timingAudio = timingAudio;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }
}

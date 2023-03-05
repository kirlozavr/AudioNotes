package com.example.audionotes.entity;

public class AudioNoteEntity {

    private long id;
    private String titleNote;
    private String dateCreateNote;
    private String timeCreateNote;
    private String filePath;
    private String fileName;
    private int timingAudio;

    public AudioNoteEntity(
            String titleNote,
            String dateCreateNote,
            String timeCreateNote,
            String filePath,
            String fileName,
            int timingAudio
    ) {
        this.titleNote = titleNote;
        this.dateCreateNote = dateCreateNote;
        this.timeCreateNote = timeCreateNote;
        this.filePath = filePath;
        this.fileName = fileName;
        this.timingAudio = timingAudio;
    }

    public AudioNoteEntity(
            long id,
            String titleNote,
            String dateCreateNote,
            String timeCreateNote,
            String filePath,
            String fileName,
            int timingAudio
    ) {
        this.id = id;
        this.titleNote = titleNote;
        this.dateCreateNote = dateCreateNote;
        this.timeCreateNote = timeCreateNote;
        this.filePath = filePath;
        this.fileName = fileName;
        this.timingAudio = timingAudio;
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
}

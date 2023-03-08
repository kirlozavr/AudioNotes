package com.example.audionotes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.audionotes.DataBase.DataBaseManager;
import com.example.audionotes.R;
import com.example.audionotes.common.ConstValues;
import com.example.audionotes.common.DateFormat;
import com.example.audionotes.common.TimeMapper;
import com.example.audionotes.entity.AudioNoteEntity;
import com.example.audionotes.service.MediaPlayService;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

public class CreateAudioNoteActivity extends AppCompatActivity {

    private boolean isPlay = false;
    private ImageButton imageButtonClose;
    private ImageButton imageButtonSave;
    private ImageButton imageButtonPlayAudioNote;
    private EditText editTextTitleNote;
    private Chronometer chronometer;
    private ProgressBar progressBar;
    private DataBaseManager dataBaseManager;
    private MediaPlayService mediaPlayService;
    private ProgressBarUpdate progressBarUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_audio_note);
        init();
    }

    private void init() {
        initView();
        onClick();

        progressBarUpdate = new ProgressBarUpdate();
        mediaPlayService = new MediaPlayService();
        dataBaseManager = new DataBaseManager(this);
        mediaPlayService.setFileAbsolutePath(
                getIntent().getStringExtra(ConstValues.KEY_FILE_PATH)
                + getIntent().getStringExtra(ConstValues.KEY_FILE_NAME)
        );
    }

    private void initView() {
        getSupportActionBar().hide();
        imageButtonClose = findViewById(R.id.imageButtonClose);
        imageButtonSave = findViewById(R.id.imageButtonSave);
        imageButtonPlayAudioNote = findViewById(R.id.imageButtonPlayAudioFragment);
        editTextTitleNote = findViewById(R.id.editTextTitleNote);
        chronometer = findViewById(R.id.timingAudioFragment);
        progressBar = findViewById(R.id.progressBarAudioPlaybackFragment);

        TimeMapper time = new TimeMapper(
                getIntent().getLongExtra(ConstValues.KEY_TIMING_AUDIO, 0),
                TimeMapper.MILLISECOND
        );
        String timing = String.format("%02d:%02d", time.getMinute(), time.getSecond());
        chronometer.setText("00:00 / " + timing);
        chronometer.setFormat("%s / " + timing);

    }


    public void onClick() {
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFile();
                closeActivity();
            }
        });

        imageButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (
                        editTextTitleNote.getText().length() != 0
                                || !editTextTitleNote.getText().toString().equals("")
                ) {
                    String title = editTextTitleNote.getText().toString();
                    String filePath = getIntent()
                            .getStringExtra(ConstValues.KEY_FILE_PATH);
                    String fileName = getIntent()
                            .getStringExtra(ConstValues.KEY_FILE_NAME);
                    String dataCreateNote = DateFormat
                            .getFormatToData()
                            .format(LocalDate.now());
                    String timeCreateNote = DateFormat.
                            getFormatToTime()
                            .format(LocalTime.now());
                    int timingAudio = mediaPlayService.length();
                    AudioNoteEntity noteEntity = new AudioNoteEntity(
                            title,
                            dataCreateNote,
                            timeCreateNote,
                            filePath,
                            fileName,
                            timingAudio
                    );

                    dataBaseManager.open();
                    dataBaseManager.insertEntity(noteEntity);
                    dataBaseManager.close();

                    closeActivity();
                } else {
                    Toast.makeText(
                            CreateAudioNoteActivity.this,
                            R.string.is_not_title,
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });

        imageButtonPlayAudioNote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isPlay) {
                    stopPlay();
                } else {
                    startPlay();
                }
            }
        });
    }

    public void startPlay() {
        isPlay = true;
        imageButtonPlayAudioNote.setBackgroundResource(R.drawable.image_button_background_grey);
        imageButtonPlayAudioNote.setImageResource(R.drawable.baseline_pause_24);
        mediaPlayService.setFileAbsolutePath(
                getIntent().getStringExtra(ConstValues.KEY_FILE_PATH)
                        + getIntent().getStringExtra(ConstValues.KEY_FILE_NAME)
        );
        mediaPlayService.start();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        progressBar.setMax(mediaPlayService.length());
        progressBar.postDelayed(progressBarUpdate, 100);
    }

    public void stopPlay() {
        isPlay = false;
        imageButtonPlayAudioNote.setBackgroundResource(R.drawable.image_button_background_light_blue);
        imageButtonPlayAudioNote.setImageResource(R.drawable.baseline_play_arrow_24);
        progressBar.removeCallbacks(progressBarUpdate);
        progressBar.setProgress(0);
        mediaPlayService.stop();
        chronometer.stop();
    }

    private void deleteFile() {
        String filePath = getIntent().getStringExtra(ConstValues.KEY_FILE_PATH);
        String fileName = getIntent().getStringExtra(ConstValues.KEY_FILE_NAME);
        File file = new File(filePath + fileName);
        file.delete();
    }

    private void closeActivity() {
        getIntent().removeExtra(ConstValues.KEY_FILE_NAME);
        getIntent().removeExtra(ConstValues.KEY_FILE_PATH);
        getIntent().removeExtra(ConstValues.KEY_TIMING_AUDIO);

        Intent intent = new Intent(this, AudioNotesActivity.class);
        startActivity(intent);
        finish();
    }

    private class ProgressBarUpdate implements Runnable {

        @Override
        public void run() {
                if (!mediaPlayService.isPlaying()) {
                    stopPlay();
                } else {
                    progressBar.setProgress(mediaPlayService.getProgress());
                    progressBar.postDelayed(this, 100);
                }
        }
    }

}
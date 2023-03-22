package com.example.audionotes.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audionotes.database.DataBaseManager;
import com.example.audionotes.R;
import com.example.audionotes.adapter.RecyclerViewAdapterAudioNotes;
import com.example.audionotes.common.ConstValues;
import com.example.audionotes.common.DateFormat;
import com.example.audionotes.dto.AudioNoteDto;
import com.example.audionotes.entity.AudioNoteEntity;
import com.example.audionotes.mapper.AudioNoteMapper;
import com.example.audionotes.service.MediaRecorderService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AudioNotesActivity extends AppCompatActivity {

    private static final String RECORD_PERMISSION = Manifest.permission.RECORD_AUDIO;
    private static final int PERMISSION_CODE = 200;
    private boolean isTouch = false;
    private boolean isPlaying = false;
    private String filePath;
    private String fileName;
    private MediaRecorderService mediaRecorderService;

    private Chronometer chronometer;
    private ImageButton imageButton;
    private ConstraintLayout constraintLayout;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterAudioNotes recyclerAdapter;
    private DataBaseManager dataBaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initRecyclerView();
    }

    public void init() {
        initView();
        onClick();
        dataBaseManager = new DataBaseManager(this);
        mediaRecorderService = new MediaRecorderService();
    }

    public void initView() {
        chronometer = findViewById(R.id.timingAudioRecord);
        imageButton = findViewById(R.id.buttonRecordAudio);
        constraintLayout = findViewById(R.id.constraintLayoutFab);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerViewAdapterAudioNotes(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void initRecyclerView() {
        AudioNoteMapper mapper = new AudioNoteMapper();
        dataBaseManager.open();
        List<AudioNoteDto> allNotesList = dataBaseManager
                .getAllEntity()
                .stream()
                .sorted(
                        Comparator.comparing(AudioNoteEntity::getDateCreateNote)
                                .thenComparing(Comparator.comparing(AudioNoteEntity::getTimeCreateNote))
                ).map(e -> mapper.getDtoToEntity(e))
                .collect(Collectors.toList());
        recyclerAdapter.setList(allNotesList);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onClick() {

        imageButton.setOnTouchListener(new View.OnTouchListener() {
            final Runnable run = new Runnable() {
                @Override
                public void run() {
                    isTouch = true;
                    startRecord();
                }
            };
            final Handler handler = new Handler();

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (checkPermissions()) {
                        handler.postDelayed(run, 800);
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP && !isTouch) {
                    handler.removeCallbacks(run);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP && isTouch) {
                    isTouch = false;
                    stopRecord();
                }
                return true;
            }
        });
    }

    public void startRecord() {
        if (checkPermissions()) {
            chronometer.setVisibility(View.VISIBLE);
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            constraintLayout.setBackground(getDrawable(R.drawable.gradient_record_audio));
            String dataTimeCreate = DateFormat
                    .getFormatToFileName()
                    .format(LocalDateTime.now());
            filePath = this.getExternalFilesDir("/").getAbsolutePath();
            fileName = "audio" + dataTimeCreate + ".3gp";
            mediaRecorderService
                    .setFilePath(filePath)
                    .setFileName(fileName)
                    .start();
        }
    }

    private void stopRecord() {
        chronometer.stop();
        chronometer.setVisibility(View.GONE);
        constraintLayout.setBackgroundResource(0);
        mediaRecorderService.stop();
        long timingAudio = SystemClock.elapsedRealtime() - this.chronometer.getBase();
        startActivity(filePath, fileName, timingAudio);
    }

    private void startActivity(String filePath, String fileName, long timingAudio) {
        Intent intent = new Intent(this, CreateAudioNoteActivity.class);
        intent.putExtra(ConstValues.KEY_FILE_PATH, filePath);
        intent.putExtra(ConstValues.KEY_FILE_NAME, fileName);
        intent.putExtra(ConstValues.KEY_TIMING_AUDIO, timingAudio);
        startActivity(intent);
    }

    private boolean checkPermissions() {
        if (
                ActivityCompat.checkSelfPermission(getApplicationContext(), RECORD_PERMISSION)
                        == PackageManager.PERMISSION_GRANTED
        ) {
            return true;
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{RECORD_PERMISSION},
                    PERMISSION_CODE
            );
            return false;
        }
    }
}
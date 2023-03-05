package com.example.audionotes.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audionotes.R;
import com.example.audionotes.common.DateFormat;
import com.example.audionotes.common.TimeMapper;
import com.example.audionotes.entity.AudioNoteEntity;
import com.example.audionotes.service.MediaPlayService;
import com.example.audionotes.service.OnProgress;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterAudioNotes
        extends RecyclerView.Adapter<RecyclerViewAdapterAudioNotes.AudioNotesHolder> {

    private LayoutInflater inflater;
    private List<AudioNoteEntity> audioNoteEntityList = new ArrayList<>();
    private MediaPlayService mediaPlayService;
    private boolean isPlay = false;
    private int index = 0;


    public RecyclerViewAdapterAudioNotes(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<AudioNoteEntity> audioNoteEntityList) {
        this.audioNoteEntityList = audioNoteEntityList;
        notifyDataSetChanged();
    }

    public void deleteList() {
        this.audioNoteEntityList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AudioNotesHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_audio_note, parent, false);
        return new AudioNotesHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(
            @NonNull AudioNotesHolder holder,
            @SuppressLint("RecyclerView") int position
    ) {
        AudioNoteEntity audioNoteEntity = audioNoteEntityList.get(position);
        String date = DateFormat
                .getFormatToData()
                .format(LocalDate.now());
        if (date.equals(audioNoteEntity.getDateCreateNote())) {
            holder.dataTimeCreateNote.setText(
                    "Сегодня в " +
                            audioNoteEntity.getTimeCreateNote()
            );
        } else {
            holder.dataTimeCreateNote.setText(
                    audioNoteEntity.getDateCreateNote() +
                            " в " +
                            audioNoteEntity.getTimeCreateNote()
            );
        }
        holder.titleNote.setText(audioNoteEntity.getTitleNote());
        TimeMapper time = new TimeMapper(
                audioNoteEntity.getTimingAudio(),
                TimeMapper.MILLISECOND
        );
        String timing = String.format("%02d:%02d", time.getMinute(), time.getSecond());
        holder.timingAudio.setText("00:00 / " + timing);
        holder.timingAudio.setFormat("%s / " + timing);

        holder.imageButtonPlayAudioNote
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.imageButtonPlayAudioNote.getTag().equals("false")) {
                            if (isPlay) {
                                mediaPlayService.stop();
                            }
                            index = position;
                            isPlay = true;
                            holder.imageButtonPlayAudioNote.setTag("true");
                            holder.imageButtonPlayAudioNote.setBackgroundResource(R.drawable.image_button_background_grey);
                            holder.imageButtonPlayAudioNote.setImageResource(R.drawable.baseline_pause_24);
                            OnProgress onProgress = new OnProgress() {
                                @Override
                                public void progress(int progress) {

                                    holder.progressBar.setProgress(progress);
                                    if (holder.progressBar.getMax() == progress) {
                                        holder.imageButtonPlayAudioNote.setTag("false");
                                        holder.imageButtonPlayAudioNote.setBackgroundResource(R.drawable.image_button_background_light_blue);
                                        holder.imageButtonPlayAudioNote.setImageResource(R.drawable.baseline_play_arrow_24);
                                        mediaPlayService.stop();
                                        holder.timingAudio.stop();
                                    }
                                }
                            };
                            mediaPlayService = new MediaPlayService(onProgress);
                            mediaPlayService.start(audioNoteEntity.getFilePath() + audioNoteEntity.getFileName());
                            holder.progressBar.setMax(mediaPlayService.length());
                            holder.timingAudio.setBase(SystemClock.elapsedRealtime());
                            holder.timingAudio.start();
                        } else {
                            isPlay = false;
                            holder.imageButtonPlayAudioNote.setTag("false");
                            holder.imageButtonPlayAudioNote.setBackgroundResource(R.drawable.image_button_background_light_blue);
                            holder.imageButtonPlayAudioNote.setImageResource(R.drawable.baseline_play_arrow_24);
                            mediaPlayService.stop();
                            holder.timingAudio.stop();
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return audioNoteEntityList.size();
    }

    public class AudioNotesHolder extends RecyclerView.ViewHolder {

        private TextView titleNote;
        private TextView dataTimeCreateNote;
        private ImageButton imageButtonPlayAudioNote;
        private Chronometer timingAudio;
        private ProgressBar progressBar;

        public AudioNotesHolder(@NonNull View itemView) {
            super(itemView);

            titleNote = itemView.findViewById(R.id.titleNotes);
            dataTimeCreateNote = itemView.findViewById(R.id.dateTimeCreateNotes);
            imageButtonPlayAudioNote = itemView.findViewById(R.id.imageButtonPlayAudioNotes);
            timingAudio = itemView.findViewById(R.id.timingAudioNotes);
            progressBar = itemView.findViewById(R.id.progressBarAudioPlaybackNotes);
        }

        public TextView getTitleNote() {
            return titleNote;
        }

        public TextView getDataTimeCreateNote() {
            return dataTimeCreateNote;
        }

        public ImageButton getImageButtonPlayAudioNote() {
            return imageButtonPlayAudioNote;
        }

        public Chronometer getTimingAudio() {
            return timingAudio;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }
    }
}

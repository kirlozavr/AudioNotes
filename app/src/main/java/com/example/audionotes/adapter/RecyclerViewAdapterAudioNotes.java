package com.example.audionotes.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audionotes.R;
import com.example.audionotes.common.DateFormat;
import com.example.audionotes.common.TimeMapper;
import com.example.audionotes.dto.AudioNoteDto;
import com.example.audionotes.service.MediaPlayService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterAudioNotes
        extends RecyclerView.Adapter<RecyclerViewAdapterAudioNotes.AudioNotesHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<AudioNoteDto> audioNotesList = new ArrayList<>();
    private MediaPlayService mediaPlayService = new MediaPlayService();
    private AudioNotesHolder playingHolder;
    private ProgressBarUpdater progressBarUpdater;
    private OnDeleteListener onDeleteListener;
    private int playingPosition = -1;


    public RecyclerViewAdapterAudioNotes(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        progressBarUpdater = new ProgressBarUpdater();
    }

    public void setList(List<AudioNoteDto> audioNotesList) {
        this.audioNotesList = audioNotesList;
        notifyDataSetChanged();
    }

    public void deleteList() {
        this.audioNotesList.clear();
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
        AudioNoteDto audioNoteDto = audioNotesList.get(position);

        if (audioNoteDto.isPlay() && position != playingPosition) {
            stop(holder);
        }

        createItemView(holder, audioNoteDto);

        holder.imageButtonPlayAudioNote
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!audioNoteDto.isPlay()) {
                            if (mediaPlayService.isPlaying()) {
                                notifyItemChanged(playingPosition);
                                mediaPlayService.stop();
                            }
                            playingPosition = position;
                            mediaPlayService.setFileAbsolutePath(
                                    audioNoteDto.getFilePath()
                                            + audioNoteDto.getFileName()
                            );
                            mediaPlayService.start();
                            play(holder);
                        } else {
                            playingPosition = -1;
                            mediaPlayService.stop();
                            stop(holder);
                        }
                    }
                });

        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DeleteDialogFragment dialogFragment = new DeleteDialogFragment(
                        context,
                        audioNoteDto.getId(),
                        audioNoteDto.getTitleNote()
                );
                dialogFragment.show(
                        ((AppCompatActivity) context).getSupportFragmentManager(),
                        "dialog"
                );
                onDeleteListener = new OnDeleteListener() {
                    @Override
                    public void onDelete() {
                        audioNotesList.remove(audioNoteDto);
                        notifyDataSetChanged();
                    }
                };
                dialogFragment.setOnDeleteListener(onDeleteListener);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return audioNotesList.size();
    }

    private void createItemView(
            @NonNull AudioNotesHolder holder,
            AudioNoteDto audioNoteDto
    ) {

        String date = DateFormat
                .getFormatToData()
                .format(LocalDate.now());
        if (date.equals(audioNoteDto.getDateCreateNote())) {
            holder.dataTimeCreateNote.setText(
                    "Сегодня в " +
                            audioNoteDto.getTimeCreateNote()
            );
        } else {
            holder.dataTimeCreateNote.setText(
                    audioNoteDto.getDateCreateNote() +
                            " в " +
                            audioNoteDto.getTimeCreateNote()
            );
        }
        holder.titleNote.setText(audioNoteDto.getTitleNote());

        TimeMapper time = new TimeMapper(
                audioNoteDto.getTimingAudio(),
                TimeMapper.MILLISECOND
        );
        String timing = String.format("%02d:%02d", time.getMinute(), time.getSecond());
        holder.timingAudio.setFormat("%s / " + timing);
        holder.timingAudio.setText("00:00 / " + timing);
        holder.progressBar.setProgress(0);
    }

    private void stop(@NonNull AudioNotesHolder holder) {
        AudioNoteDto noteDto = audioNotesList.get(holder.getAdapterPosition());
        noteDto.setPlay(false);
        audioNotesList.set(holder.getAdapterPosition(), noteDto);
        holder.timingAudio.stop();
        holder.progressBar.removeCallbacks(progressBarUpdater);
        holder.progressBar.setProgress(0);
        holder.imageButtonPlayAudioNote.setBackgroundResource(R.drawable.image_button_background_light_blue);
        holder.imageButtonPlayAudioNote.setImageResource(R.drawable.baseline_play_arrow_24);
    }

    private void play(@NonNull AudioNotesHolder holder) {
        playingHolder = holder;
        AudioNoteDto noteDto = audioNotesList.get(holder.getAdapterPosition());
        noteDto.setPlay(true);
        audioNotesList.set(holder.getAdapterPosition(), noteDto);
        holder.progressBar.setMax(mediaPlayService.length());
        holder.progressBar.postDelayed(progressBarUpdater, 100);
        holder.timingAudio.setBase(SystemClock.elapsedRealtime());
        holder.timingAudio.start();
        holder.imageButtonPlayAudioNote.setBackgroundResource(R.drawable.image_button_background_grey);
        holder.imageButtonPlayAudioNote.setImageResource(R.drawable.baseline_pause_24);
    }

    private class ProgressBarUpdater implements Runnable {
        @Override
        public void run() {
            if (playingHolder != null) {
                if (!mediaPlayService.isPlaying()) {
                    stop(playingHolder);
                } else {
                    playingHolder.progressBar.setProgress(mediaPlayService.getProgress());
                    playingHolder.progressBar.postDelayed(this, 100);
                }
            }
        }
    }

    public class AudioNotesHolder extends RecyclerView.ViewHolder {

        private TextView titleNote;
        private TextView dataTimeCreateNote;
        private ImageButton imageButtonPlayAudioNote;
        private Chronometer timingAudio;
        private ProgressBar progressBar;
        private ConstraintLayout constraintLayout;

        public AudioNotesHolder(@NonNull View itemView) {
            super(itemView);

            titleNote = itemView.findViewById(R.id.titleNotes);
            dataTimeCreateNote = itemView.findViewById(R.id.dateTimeCreateNotes);
            imageButtonPlayAudioNote = itemView.findViewById(R.id.imageButtonPlayAudioNotes);
            timingAudio = itemView.findViewById(R.id.timingAudioNotes);
            progressBar = itemView.findViewById(R.id.progressBarAudioPlaybackNotes);
            constraintLayout = itemView.findViewById(R.id.constraintLayoutItemAudioNote);
        }
    }
}

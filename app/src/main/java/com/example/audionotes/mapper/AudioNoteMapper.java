package com.example.audionotes.mapper;

import com.example.audionotes.dto.AudioNoteDto;
import com.example.audionotes.entity.AudioNoteEntity;

public class AudioNoteMapper implements Mapper<AudioNoteEntity, AudioNoteDto>{

    @Override
    public AudioNoteEntity getEntityToDto(AudioNoteDto dto) {
        return new AudioNoteEntity(
                dto.getId(),
                dto.getTitleNote(),
                dto.getDateCreateNote(),
                dto.getTimeCreateNote(),
                dto.getFilePath(),
                dto.getFileName(),
                dto.getTimingAudio()
        );
    }

    @Override
    public AudioNoteDto getDtoToEntity(AudioNoteEntity entity) {
        return new AudioNoteDto(
                entity.getId(),
                entity.getTitleNote(),
                entity.getDateCreateNote(),
                entity.getTimeCreateNote(),
                entity.getFilePath(),
                entity.getFileName(),
                entity.getTimingAudio(),
                false
        );
    }
}

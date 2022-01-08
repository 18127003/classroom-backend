package com.example.demo.mapper;

import com.example.demo.dto.ParticipantDto;
import com.example.demo.entity.Participant;
import com.example.demo.mapper.decorator.ParticipantDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(ParticipantDecorator.class)
public interface ParticipantMapper {
    ParticipantDto toParticipantDto(Participant participant);
}

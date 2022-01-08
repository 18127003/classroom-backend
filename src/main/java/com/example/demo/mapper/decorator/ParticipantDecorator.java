package com.example.demo.mapper.decorator;

import com.example.demo.dto.ParticipantDto;
import com.example.demo.entity.Participant;
import com.example.demo.mapper.ParticipantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ParticipantDecorator implements ParticipantMapper {
    @Autowired
    @Qualifier("delegate")
    private ParticipantMapper delegate;

    @Override
    public ParticipantDto toParticipantDto(Participant participant) {
        var user = delegate.toParticipantDto(participant);
        var account = participant.getAccount();
        user.setAccountId(account.getId());
        user.setName(account.getName());
        user.setEmail(account.getEmail());
        if(account.getStudentInfo()!=null){
            user.setStudentId(account.getStudentInfo().getStudentId());
        }
        return user;
    }
}

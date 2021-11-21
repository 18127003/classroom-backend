package com.example.demo.security;

import com.example.demo.entity.Classroom;
import com.example.demo.entity.Participant;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class ParticipantInfo {
    private Participant participant;
    public Classroom getClassroom(){return participant.getClassroom();}
    public void setParticipant(Participant participant){
        this.participant = participant;
    }
}

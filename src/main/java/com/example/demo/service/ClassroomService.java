package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.Participant;

import java.util.List;

public interface ClassroomService {
    List<Participant> getClassrooms(Long accountId);

    Classroom createClassroom(Classroom classroom, Account account);

    List<Participant> getParticipants(Long classroomId);
}

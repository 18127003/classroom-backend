package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.Participant;

import java.util.List;

public interface ClassroomService {
    List<Participant> getAssignedClassrooms(Long accountId);

    Classroom createClassroom(Classroom classroom, Account account);

    Classroom joinClassroom(String code, Account account);

    List<Participant> getParticipants(Long classroomId);
}

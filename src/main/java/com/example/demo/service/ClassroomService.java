package com.example.demo.service;

import com.example.demo.common.enums.Role;
import com.example.demo.entity.Account;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.Participant;

import java.util.List;

public interface ClassroomService {
    List<Participant> getAssignedClassrooms(Long accountId);

    Participant createClassroom(Classroom classroom, Account account);

    Participant joinClassroom(String code, Role role, Account account);

    List<Participant> getParticipants(Long classroomId);

    void sendInvitation(List<String> invitations, Long classroomId, Role role);

    Participant getAssignedClassroom(Long classroomId, Account account);
}

package com.example.demo.service;

import com.example.demo.common.enums.Role;
import com.example.demo.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ClassroomService {
    Classroom getClassroom(Long classroomId);

    List<Participant> getAssignedClassrooms(Long accountId);

    Participant createClassroom(Classroom classroom, Account account);

    Participant joinClassroom(String code, Role role, Account account);

    void removeParticipants(Long id, List<Long> removals);

    void hideParticipants(Long id, List<Long> participants);

    String regenerateCode(Long id);

    List<Participant> getParticipants(Long classroomId);

    void sendInvitation(List<String> invitations, Long classroomId, Role role);

    Participant getAssignedClassroom(Long classroomId, Long accountId);

    StudentInfoClassroom getStudentInfo(String studentId, Long classroomId);

    List<StudentInfoClassroom> getAllStudentInfo(Long classroomId);

    void importStudentInfo(MultipartFile file, Classroom classroom) throws IOException;
}

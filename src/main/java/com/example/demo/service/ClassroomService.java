package com.example.demo.service;

import com.example.demo.common.enums.Role;
import com.example.demo.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ClassroomService {
    Classroom getClassroom(Long classroomId);

    List<Participant> getAssignedClassrooms(UUID accountId);

    Participant createClassroom(Classroom classroom, Account account);

    Participant joinClassroom(String code, Role role, Account account);

    void removeParticipants(Long id, List<UUID> removals);

    void hideParticipants(Long id, List<UUID> participants);

    String regenerateCode(Long id);

    List<Participant> getParticipants(Long classroomId);

    void sendInvitation(String frontHost, List<String> invitations, Long classroomId, Role role);

    Participant getAssignedClassroom(Long classroomId, UUID accountId);

    StudentInfoClassroom getStudentInfo(String studentId, Long classroomId);

    List<StudentInfoClassroom> getAllStudentInfo(Long classroomId);

    void importStudentInfo(MultipartFile file, Classroom classroom) throws IOException;

    List<Classroom> getAllClassroom(boolean sortDesc);
}

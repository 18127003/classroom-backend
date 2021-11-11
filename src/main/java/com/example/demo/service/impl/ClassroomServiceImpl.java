package com.example.demo.service.impl;

import com.example.demo.common.enums.Role;
import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.entity.Account;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.Participant;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.repository.ParticipantRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final AccountService accountService;
    private final ParticipantRepository participantRepository;
    private final StringEncryptor stringEncryptor;

    @Override
    public List<Participant> getClassrooms(Long accountId) {
        var account = accountService.findById(accountId);
        return account.getAssignedClasses();
    }

    @Override
    public Classroom createClassroom(Classroom classroom, Account account) {
        classroom.setCreator(account);
        var participant = participantRepository.save(new Participant(account, classroom, Role.TEACHER));
        var createdClassroom = participant.getClassroom();
        createdClassroom.setCode(stringEncryptor.encrypt(createdClassroom.getId().toString()));
        return createdClassroom;
    }

    @Override
    public List<Participant> getParticipants(Long classroomId) {
        var classroom = classroomRepository.findById(classroomId)
                .orElseThrow(()->new RTException(new RecordNotFoundException(classroomId.toString(),Classroom.class.getSimpleName())));
        return classroom.getParticipants();
    }
}

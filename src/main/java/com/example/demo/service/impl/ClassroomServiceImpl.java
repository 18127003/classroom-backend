package com.example.demo.service.impl;

import com.example.demo.common.enums.Role;
import com.example.demo.common.exception.DuplicateRecordException;
import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.entity.Account;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.Participant;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.repository.ParticipantRepository;
import com.example.demo.service.ClassroomService;
import com.example.demo.util.EmailSender;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static com.example.demo.common.constant.Constants.HOST_EMAIL;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final ParticipantRepository participantRepository;
    private final StringEncryptor stringEncryptor;
    private final EmailSender emailSender;

    @Override
    public List<Participant> getAssignedClassrooms(Long accountId) {
        return participantRepository.getAssignedClassroom(accountId);
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
    public Classroom joinClassroom(String code, Account account) {
        var classroom = classroomRepository.findByCode(code);
        if (classroom == null){
            throw new RTException(new RecordNotFoundException(code, Classroom.class.getSimpleName()));
        }
        var participant = participantRepository.findParticipant(classroom.getId(), account.getId());
        if (participant != null){
            throw new RTException(new DuplicateRecordException(participant.getId().toString(), Participant.class.getSimpleName()));
        }
        participantRepository.save(new Participant(account, classroom, Role.STUDENT));
        return classroom;
    }

    @Override
    public List<Participant> getParticipants(Long classroomId) {
        return participantRepository.getParticipants(classroomId);
    }

    @Override
    public void sendInvitation(List<String> invitations, Long classroomId, Role role) {
        var classroom = classroomRepository.findById(classroomId)
                .orElseThrow(()->new RTException(
                        new RecordNotFoundException(classroomId.toString(), Classroom.class.getSimpleName())));

        var invitationLink = generateInvitationLink(classroom, role);

        //Prepare subject & content
        var content = "Please follow the link to accept "+role.toString().toLowerCase()+ " invitation "+invitationLink;
        var subject = "Classroom invitation for "+classroom.getName();

        //send mails
        invitations.forEach(email-> {
            try {
                emailSender.sendEmail(HOST_EMAIL, email, subject, content);
            } catch (IOException ignored) { }
        });
    }

    private String generateInvitationLink(Classroom classroom, Role role){
//        return "http://localhost:8085/invite/accpet_token/"
        return "https://18127003.github.io/classroom-frontend/invite/accept_token/"
                +classroom.getId()
                +"?role="
                +role.toString()
                +"&token="
                +classroom.getCode();
    }

    @Override
    public Participant getAssignedClassroom(Long classroomId, Account account) {
        var participant = participantRepository.findParticipant(classroomId, account.getId());
        if(participant==null){
            throw new RTException(new RecordNotFoundException(classroomId.toString(), Classroom.class.getSimpleName()));
        }
        return participant;
    }
}

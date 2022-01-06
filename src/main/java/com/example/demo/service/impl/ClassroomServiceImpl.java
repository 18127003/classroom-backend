package com.example.demo.service.impl;

import com.example.demo.common.enums.Role;
import com.example.demo.common.exception.DuplicateRecordException;
import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.entity.*;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.repository.ParticipantRepository;
import com.example.demo.repository.StudentInfoClassroomRepository;
import com.example.demo.repository.StudentInfoRepository;
import com.example.demo.service.ClassroomService;
import com.example.demo.util.EmailSender;
import com.example.demo.util.ExcelUtil;
import com.example.demo.util.dto.StudentInfoData;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.common.constant.Constants.HOST_EMAIL;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final ParticipantRepository participantRepository;
    private final StudentInfoRepository studentInfoRepository;
    private final StudentInfoClassroomRepository studentInfoClassroomRepository;
    private final StringEncryptor stringEncryptor;
    private final EmailSender emailSender;
    private final ExcelUtil excelUtil;

    @Override
    public Classroom getClassroom(Long classroomId) {
        return classroomRepository.findById(classroomId)
                .orElseThrow(()->new RTException(new RecordNotFoundException(classroomId.toString(), Classroom.class.getSimpleName())));
    }

    @Override
    public List<Participant> getAssignedClassrooms(Long accountId) {
        return participantRepository.getAssignedClassrooms(accountId);
    }

    @Override
    public Participant createClassroom(Classroom classroom, Account account) {
        classroom.setCreator(account);
        var participant = participantRepository.save(new Participant(account, classroom, Role.TEACHER, false));
        var createdClassroom = participant.getClassroom();
        createdClassroom.setCode(stringEncryptor.encrypt(createdClassroom.getId().toString()));
        return participant;
    }

    @Override
    public Participant joinClassroom(String code, Role role, Account account) {
        var classroom = classroomRepository.findByCode(code);
        if (classroom == null){
            throw new RTException(new RecordNotFoundException(code, Classroom.class.getSimpleName()));
        }
        var participant = participantRepository.findParticipant(classroom.getId(), account.getId());
        if (participant != null){
            throw new RTException(new DuplicateRecordException(participant.getId().toString(), Participant.class.getSimpleName()));
        }
        return participantRepository.save(new Participant(account, classroom, role, false));
    }

    @Override
    public void removeParticipants(Long id, List<Long> removals) {
        var removed = getParticipantsWithId(id, removals);
        participantRepository.deleteAll(removed);
    }

    @Override
    public void hideParticipants(Long id, List<Long> participants) {
        List<Participant> hidings = getParticipantsWithId(id, participants);
        hidings.forEach(hiding->hiding.setHidden(true));
        participantRepository.saveAll(hidings);
    }

    private List<Participant> getParticipantsWithId(Long id, List<Long> participants) {
        var allParticipants = participantRepository.getParticipants(id);
        if(allParticipants.isEmpty()){
            throw new RTException(new RecordNotFoundException(id.toString(), Classroom.class.getSimpleName()));
        }
        return  allParticipants.stream()
                .filter(participant -> participants.contains(participant.getAccount().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public String regenerateCode(Long id) {
        var classroom = classroomRepository.findById(id)
                .orElseThrow(()->new RTException(
                        new RecordNotFoundException(id.toString(), Classroom.class.getSimpleName())));
        var newCode = stringEncryptor.encrypt(classroom.getCode());
        classroom.setCode(newCode);
        classroomRepository.save(classroom);
        return newCode;
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
//        return "http://localhost:8085/#/invite/accept_token/"
        return "https://18127003.github.io/classroom-frontend/#/invite/accept_token/"
                +classroom.getId()
                +"?role="
                +role.toString()
                +"&code="
                +classroom.getCode();
    }

    @Override
    public Participant getAssignedClassroom(Long classroomId, Long accountId) {
        var participant = participantRepository.findParticipant(classroomId, accountId);
        if(participant==null){
            throw new RTException(new RecordNotFoundException(classroomId.toString(), Classroom.class.getSimpleName()));
        }
        return participant;
    }

    @Override
    public StudentInfoClassroom getStudentInfo(String studentId, Long classroomId) {
        var result = studentInfoClassroomRepository.findByStudentId(studentId, classroomId);
        if(result == null){
            throw new RTException(new RecordNotFoundException(studentId, StudentInfo.class.getSimpleName()));
        }
        return result;
    }


    @Override
    public List<StudentInfoClassroom> getAllStudentInfo(Long classroomId) {
        return studentInfoClassroomRepository.findAllStudentInfo(classroomId);
    }

    @Override
    public void importStudentInfo(MultipartFile file, Classroom classroom) throws IOException {
        var students = excelUtil.importStudentInfo(file);

        // get existed system student info
        var existedInfo = studentInfoRepository.findByListStudentId(
                students.stream().map(StudentInfoData::getStudentId).collect(Collectors.toList()));

        var existedIds = existedInfo.stream().map(StudentInfo::getStudentId)
                .collect(Collectors.toList());

        var newInfo = students.stream().filter(info->!existedIds.contains(info.getStudentId()))
                .map(info->new StudentInfo(info.getStudentId(), info.getName()))
                .collect(Collectors.toList());
        // save new infos
        newInfo = studentInfoRepository.saveAll(newInfo);
        newInfo.addAll(existedInfo);

        // get existed classroom student info
        var existedClassInfo = studentInfoClassroomRepository.findByListStudentId(
                newInfo.stream().map(StudentInfo::getStudentId).collect(Collectors.toList()), classroom.getId()
        );

        var infosWithExistedClassInfo = existedClassInfo.stream()
                .map(StudentInfoClassroom::getStudentInfo).collect(Collectors.toList());

        var newClassInfo = newInfo.stream().filter(info->!infosWithExistedClassInfo.contains(info))
                .map(info->new StudentInfoClassroom(classroom, info))
                .collect(Collectors.toList());

        studentInfoClassroomRepository.saveAll(newClassInfo);
    }
}
